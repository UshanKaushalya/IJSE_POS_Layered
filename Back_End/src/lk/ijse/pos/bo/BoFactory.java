package lk.ijse.pos.bo;

import lk.ijse.pos.bo.custom.impl.CustomerBoImpl;
import lk.ijse.pos.bo.custom.impl.ItemBoImpl;

public class BoFactory {
    private static BoFactory boFactory;
    private BoFactory(){
    }
    public static BoFactory getBoFactory(){
        return (boFactory==null)? boFactory=new BoFactory() : boFactory;
    }

    public enum BOTypes{
        CUSTOMER, ITEM
    }

    public SuperBo getBO(BOTypes types){
        switch (types){
            case CUSTOMER:
                return new CustomerBoImpl();
            case ITEM:
                return new ItemBoImpl();
            default:
                return null;
        }
    }
}
