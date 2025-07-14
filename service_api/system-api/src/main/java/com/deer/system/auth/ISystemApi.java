package com.deer.system.auth;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.deer.entities.system.SysUser;

//import static com.deer.base.common.constants.GeneralConstants.CONSTANTS_SYSTEM;

@FeignClient(name = "system-service", path = "/sysTest")
public interface ISystemApi {

    @PostMapping("sysTestGetUser")
    SysUser sysTestGetUser(@RequestParam("userId") String userId);

}
