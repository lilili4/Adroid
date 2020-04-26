package edu.fjnu.actionmode;

public class Item {
    private String name;//显示的选项名
    private boolean is_chooced;//记录是否被选中

    //带两个参数的构造函数
    public Item(String name, boolean is_chooced){
        super();
        this.name = name;
        this.is_chooced = is_chooced;
    }

    public String getName() {
        return name;
    }
    public boolean isState() {
        return is_chooced;
    }
    public void setState(boolean is_chooced) {
        this.is_chooced = is_chooced;
    }

}