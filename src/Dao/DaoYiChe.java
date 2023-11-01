package Dao;

public class DaoYiChe extends DaoFather {

    public DaoYiChe(int choseDB, int choseTable) {
        super(choseDB, choseTable);
    }

    //更新下载状态
    public void Method_ChangeState(int key){
        String sql = "update "+tableName+" set  C_DownState = '是' where "+primaryKey+" ="+key;
        MethodIUD(sql);
    }


}
