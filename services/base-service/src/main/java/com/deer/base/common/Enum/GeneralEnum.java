package com.deer.base.common.Enum;


public interface GeneralEnum {
    //region 用户账号锁
    enum LOCKED_TYPE {
        LOCK(1,"锁定"),
        UNLOCK(0,"可用")
        ;

        public static String getDescByIntVal(Integer intVal){
            switch (null == intVal ? -1:intVal) {
                case 1 : return "锁定";
                case 0 : return "可用";
            }
            return "";
        }

        public int intVal;
        public String strVal;
        public String desc;

        LOCKED_TYPE(int v, String desc){
            intVal = v;
            strVal = String.valueOf(v);
            this.desc = desc;
        }
    }
    //endregion

}