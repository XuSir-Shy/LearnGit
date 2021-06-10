package my.pojo;

public class Mypage {
    private int page=1;
    //定义每一页显示的数据条数,并赋初值为2，表示每页两条数据
    private int row=2;

    private Integer num=2;
    //查询条件
    private String condition;
    //查询参数
    private String conValue;

    //修改密码所用参数
    private String oldPwd;
    private String newPwd;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConValue() {
        if(conValue==null)
        {
            return "";
        }
        return conValue;
    }

    public void setConValue(String conValue) {
        this.conValue = conValue;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
