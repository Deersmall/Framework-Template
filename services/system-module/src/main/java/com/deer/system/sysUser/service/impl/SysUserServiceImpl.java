package com.deer.system.sysUser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deer.entities.system.SysMenu;
import com.deer.entities.system.SysRole;
import com.deer.entities.system.SysUser;
import com.deer.framework.utils.EncryptUtils;
import com.deer.framework.utils.SecurityUtils;
import com.deer.system.sysMenu.service.ISysMenuService;
import com.deer.system.sysRole.service.ISysRoleService;
import com.deer.system.sysUser.mapper.SysUserMapper;
import com.deer.system.sysUser.service.ISysUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysMenuService iSysMenuService;

    @Override
    public IPage<SysUser> getUserList(SysUser sysUser) {

        IPage<SysUser> page = new Page<>(sysUser.getPageNum(), sysUser.getPageSize());
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(sysUser.getUserName())){
            lambdaQueryWrapper.like(SysUser::getUserName,sysUser.getUserName());
        }
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(sysUser.getNickName())){
            lambdaQueryWrapper.like(SysUser::getNickName, sysUser.getNickName());
        }
        if (com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty(sysUser.getStatus())){
            lambdaQueryWrapper.eq(SysUser::getStatus,sysUser.getStatus());
        }else {
            lambdaQueryWrapper.ne(SysUser::getStatus,-1);
        }

        IPage<SysUser> userPage = sysUserMapper.selectPage(page, lambdaQueryWrapper);
        userPage.getRecords().forEach(user -> {
        });

        return userPage;
    }

    @Override
    public SysUser sysUserByUserName(String userName) {
//        设置查询条件
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUserName,userName);

//        查询用户
        SysUser sysUser = sysUserMapper.selectOne(lambdaQueryWrapper);
//        判断用户是否存在
        if(ObjectUtils.isEmpty(sysUser)) throw new RuntimeException("未查询到系统用户：" + userName);

//        查询用户绑定角色
        Set<SysRole> roles = iSysRoleService.getRolesByUserId(sysUser.getUserId());
//        判断用户是否拥有角色
        if (CollectionUtils.isEmpty(roles)) throw new RuntimeException(userName + "账号还未分配角色");

//        查询用户可用权限及菜单
        Map<Integer, List<SysMenu>> menusByType = iSysMenuService.getMenusByRoleIds(
//                角色Ids
                roles.stream()
                        .filter(x -> StringUtils.isNotEmpty(x.getRoleId()))
                        .map(SysRole::getRoleId)
                        .collect(Collectors.toList())
        ).stream().collect(Collectors.groupingBy(SysMenu::getMenuType));

//        用户角色
        sysUser.setRoles(roles);
//        用户权限
        sysUser.setPermissionList(menusByType.get(2).stream().map(SysMenu::getPermission).collect(Collectors.toList()));
//        用户菜单
        sysUser.setMenus(getMenuTree(menusByType));

        return sysUser;
    }

    @Override
    @Transactional
    public int add(SysUser sysUser) {
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, sysUser.getUserName()));
        if (user != null){
           throw new RuntimeException("账号已存在");
        }

        String uuid = UUID.randomUUID().toString();
        String encryptPass = SecurityUtils.encryptPassword(EncryptUtils.encrypt("123456", uuid));

        sysUser.setUserId(UUID.randomUUID().toString());
        sysUser.setSalt(uuid);
        sysUser.setPassword(encryptPass);

        sysUser.setCreateById(SecurityUtils.getUserId());
        sysUser.setCreateTime(System.currentTimeMillis());

        return sysUserMapper.insert(sysUser);
    }

    @Override
    @Transactional
    public int upd(SysUser sysUser) {
//        String uuid = StringUtils.isNotEmpty(sysUser.getSalt())?sysUser.getSalt():UUID.randomUUID().toString();
//        String encryptPass = EncryptUtils.encrypt(sysUser.getPassword(), uuid);
//
//        sysUser.setSalt(uuid);
//        sysUser.setPassword(encryptPass);
        sysUser.setPassword(null);  //  不修改密码
        sysUser.setUpdateById(SecurityUtils.getUserId());
        sysUser.setUpdateTime(System.currentTimeMillis());

        return sysUserMapper.updateById(sysUser);
    }

    @Override
    @Transactional
    public int updatePassword(SysUser sysUser) {
        String uuid = StringUtils.isNotEmpty(sysUser.getSalt())?sysUser.getSalt():UUID.randomUUID().toString();
        String encryptPass = SecurityUtils.encryptPassword(EncryptUtils.encrypt(sysUser.getPassword(), uuid));

        sysUser.setSalt(uuid);
        sysUser.setPassword(encryptPass);

        sysUser.setUpdateById(SecurityUtils.getUserId());
        sysUser.setUpdateTime(System.currentTimeMillis());

        return sysUserMapper.updateById(sysUser);
    }

    @Override
    public void userTemplateDownload(HttpServletResponse response) {
//        Excel文件生成区域
        ServletOutputStream outputStream = null;
//        创建新的Excel工作簿（XSSFWorkbook用于.xlsx格式）
        XSSFWorkbook workbook = new XSSFWorkbook();


//        sheet工作表
        XSSFSheet sheet1 = workbook.createSheet("用户信息导入模版");
        XSSFSheet sheet2 = workbook.createSheet("下拉框数据sheet");

//        设置列宽
        sheet1.setDefaultColumnWidth((short) 25);
        sheet2.setDefaultColumnWidth((short) 25);

//        创建Excel工作簿样式
        XSSFCellStyle style = workbook.createCellStyle();
//        换行
        style.setWrapText(true);

        /** sheet1工作表 */

        // region 标题行
//        创建标题行（第1行）
        XSSFRow row0 = sheet1.createRow(0);
        row0.setHeightInPoints(30);                             //  行高
        XSSFCell cell0 = row0.createCell(0);        //  创建单元格
        cell0.setCellValue("用户导入模版");              //  单元格内容
        cell0.setCellStyle(style);                            //  单元格样式
//        合并单元格         firstRow起始行 , lastRow结束行 , firstCol起始列 , lastCol结束列
        CellRangeAddress titleRow = new CellRangeAddress(0, 0, 0, 6);
        sheet1.addMergedRegion(titleRow);
        // endregion

        // region 说明行
        String str = "注意:\n" +
                "用户信息请按规范填写；\n" +
                "1.所有名称最大支持30个中文字符；\n" +
                "2.带*为必填项";

//        创建说明行（第2行）
        XSSFRow row1 = sheet1.createRow(1);
        row1.setHeightInPoints(80);                            //  行高
        XSSFCell cell1 = row1.createCell(0);        //  创建单元格
        cell1.setCellStyle(style);                             //  单元格样式
        cell1.setCellValue(str);                              //  单元格内容
//        合并单元格         firstRow起始行 , lastRow结束行 , firstCol起始列 , lastCol结束列
        CellRangeAddress descRow = new CellRangeAddress(1, 1, 0, 6);
        sheet1.addMergedRegion(descRow);
        // endregion

        // region 表头行
//        创建表头行（第3行）
        XSSFRow row2 = sheet1.createRow(2);
        String[] th = new String[]{"用户账号*","用户名称*","生日日期","用户密码*","用户状态*"};
//        行高
        row2.setHeightInPoints(15);
//        各单元格样式及内容
        for (int i=0;i<th.length;i++){
            XSSFRichTextString text = new XSSFRichTextString(th[i]);

            XSSFCell cell = row2.createCell(i);     //  创建单元格
            cell.setCellStyle(style);               //  单元格样式
            cell.setCellValue(text);                //  单元格内容
        }
        // endregion

        /** sheet2工作表 */
//        创建表头行
        XSSFRow sheet2Row = sheet2.createRow(0);
        String[] sheet2Ts=new String[]{"原材料编号","原材料名称","原材料大类编码","原材料大类名称","原材料细类编码","原材料细类名称"};
//        行高
        sheet2Row.setHeightInPoints(15);
//        各单元格样式及内容
        for (int i=0;i<sheet2Ts.length;i++){
            XSSFRichTextString text = new XSSFRichTextString(sheet2Ts[i]);

            XSSFCell cell = sheet2Row.createCell(i);    //  创建单元格
            cell.setCellStyle(style);                   //  单元格样式
            cell.setCellValue(text);                    //  单元格内容

        }

        List<SysUser> sysUsers = sysUserMapper.selectList(new LambdaQueryWrapper<>());

//        写入各列数据
        for (int i = 0; i < sysUsers.size(); i++) {
            XSSFRow row = sheet2.createRow(i+1);
            row.createCell(0).setCellValue(sysUsers.get(i).getUserId());
            row.createCell(1).setCellValue(sysUsers.get(i).getUserName());
            row.createCell(2).setCellValue(sysUsers.get(i).getNickName());
            row.createCell(3).setCellValue(sysUsers.get(i).getPassword());
            row.createCell(4).setCellValue(sysUsers.get(i).getSalt());
            row.createCell(5).setCellValue(sysUsers.get(i).getStatus());
        }

        /** 为sheet1的原材料名称列添加下拉框 */

        // 获取数据验证帮助器
        DataValidationHelper validationHelper = sheet1.getDataValidationHelper();

        // 创建公式：引用sheet2的B列（原材料名称列），从第2行开始
        // '原材料信息'!$B$2:$B$1000 表示引用sheet2的B2到B1000单元格
        // 这里假设最多有999条原材料数据（从第2行到第1000行）
        String formula = "'" + sheet2.getSheetName() + "'!$B$2:$B$1000";

        // 如果需要动态引用所有有数据的行，可以使用以下公式（需要POI 5.2.0+支持）
        // String dynamicFormula = "'" + sheet2.getSheetName() + "'!$B$2:$B$" + (rawMaterials.size() + 1);

        // 创建约束
        DataValidationConstraint constraint = validationHelper.createFormulaListConstraint(formula);

        // 设置数据验证的范围：sheet1的E列（第5列，索引4），从第4行开始（索引3）到第1000行
        CellRangeAddressList addressList = new CellRangeAddressList(
                3, // 起始行（第4行，索引3）
                1000, // 结束行（第1001行，索引1000）
                7, // 起始列（E列，索引4）
                7  // 结束列（E列，索引4）
        );

        // 创建数据验证
        DataValidation validation = validationHelper.createValidation(constraint, addressList);

        // 设置验证选项
        validation.setShowErrorBox(true); // 显示错误框
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP); // 错误样式：停止
        validation.createErrorBox("输入错误", "请从下拉列表中选择有效的原材料名称"); // 错误提示

        validation.setShowPromptBox(true);  // 显示提示框
        validation.createPromptBox("选择提示", "请从下拉列表中选择原材料名称");

        // 将验证应用到工作表
        sheet1.addValidationData(validation);

        String[] list13=new String[]{"启用","禁用"};
        DataValidationConstraint constraint2 = validationHelper.createExplicitListConstraint(list13);
        CellRangeAddressList addressList2 = new CellRangeAddressList(3,1003,4,4);
        DataValidation validation2 = validationHelper.createValidation(constraint2, addressList2);
        validation2.setShowErrorBox(true); // 显示错误框
        validation2.setErrorStyle(DataValidation.ErrorStyle.STOP); // 错误样式：停止
        validation2.createErrorBox("输入错误", "请选择'启用'或'禁用'"); // 错误提示

        validation2.setShowPromptBox(true);  // 显示提示框
        validation2.createPromptBox("状态选择", "请选择用户状态：启用或禁用");

        sheet1.addValidationData(validation2);


        /** 文件输出部分 */
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            workbook.close();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
                if(outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }

        //endregion
    }

    @Override
    @Transactional
    public void userImport(MultipartFile file) {
        try {
//             获取上传文件的输入流
            InputStream inputStream = file.getInputStream();

//             创建一个工作簿（Workbook）对象
            Workbook workbook = new XSSFWorkbook(inputStream);

//             获取第一个Sheet页
            Sheet sheet = workbook.getSheetAt(0);

//            将读取的数据写入此集合
            List<SysUser> sysUserList = new ArrayList<>();

//            遍历每行 获取Excel数据写入 sysUserList
            for (Row row : sheet) {
//                从第四行开始读取
                if (row.getRowNum() >= 3){
//                    行数据处理
                    SysUser sysUser = handleRowData(row);
                    sysUserList.add(sysUser);
                }
            }

//            向数据库确认数据是否存在
            List<SysUser> existsUser = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().in(
                            SysUser::getUserName, sysUserList.stream().map(SysUser::getUserName).collect(Collectors.toList())));

//            数据库存在该账号 抛出异常 账号+message
            if (!CollectionUtils.isEmpty(existsUser)){
                List<String> existsUserName = existsUser.stream().map(SysUser::getUserName).collect(Collectors.toList());
                throw new RuntimeException(existsUserName + "账号已存在");
            }

            sysUserMapper.batchInsert(sysUserList);

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取菜单树
     * @param menusByType
     * @return
     */
    private List<SysMenu> getMenuTree(Map<Integer, List<SysMenu>> menusByType) {

//        组装目录菜单结构
        List<SysMenu> directories = menusByType.getOrDefault(0, new ArrayList<>());
        List<SysMenu> menus = menusByType.getOrDefault(1, new ArrayList<>());
        List<SysMenu> permissions = menusByType.getOrDefault(2, new ArrayList<>());

        // 创建菜单ID到菜单对象的映射，便于快速查找
        Map<String, SysMenu> menuMap = new HashMap<>();

        // 处理目录（第一层）
        for (SysMenu directory : directories) {
            directory.setChildren(new ArrayList<>());
            menuMap.put(directory.getMenuId(), directory);
        }

        // 处理菜单（第二层），挂载到对应目录下
        for (SysMenu menu : menus) {
            menu.setChildren(new ArrayList<>());
            if (menu.getParentId() != null && menuMap.containsKey(menu.getParentId())) {
                menuMap.get(menu.getParentId()).addChild(menu);
            }
            menuMap.put(menu.getMenuId(), menu);
        }

        // 处理权限（第三层），挂载到对应菜单下
        for (SysMenu permission : permissions) {
            if (permission.getParentId() != null && menuMap.containsKey(permission.getParentId())) {
                menuMap.get(permission.getParentId()).addChild(permission);
            }
        }

        return directories
                .stream()
                .sorted(Comparator.comparingInt(SysMenu::getOrderNum))
                .collect(Collectors.toList());
    }

    private SysUser handleRowData(Row row) {
        SysUser sysUser = new SysUser();

//                    必填非空判断
        if (StringUtils.isEmpty(row.getCell(0).getStringCellValue())) throw new RuntimeException("用户账号字段必填");
        if (StringUtils.isEmpty(row.getCell(1).getStringCellValue())) throw new RuntimeException("用户名称字段必填");
        if (StringUtils.isEmpty(row.getCell(4).getStringCellValue())) throw new RuntimeException("用户密码字段必填");

//                    处理数据 写入实体类

        String salt = UUID.randomUUID().toString();
        String encryptPass = SecurityUtils.encryptPassword(EncryptUtils.encrypt(row.getCell(3).getStringCellValue(), salt));


        sysUser.setUserId(UUID.randomUUID().toString());
        sysUser.setUserName(row.getCell(0).getStringCellValue());
        sysUser.setNickName(row.getCell(1).getStringCellValue());
        sysUser.setPassword(encryptPass);
        sysUser.setSalt(salt);
        sysUser.setStatus(row.getCell(4).getStringCellValue().equals("启用") ? 0 : 1);
        sysUser.setCreateById(SecurityUtils.getUserId());
        sysUser.setCreateTime(System.currentTimeMillis());


        if (!ObjectUtils.isEmpty(row.getCell(2).getDateCellValue())) {
            sysUser.setBirthdayDate(row.getCell(2).getDateCellValue().getTime());
            sysUser.setUserAge(LocalDateTime.now().getYear() - row.getCell(2).getLocalDateTimeCellValue().getYear());
        }


        return sysUser;
    }
}




