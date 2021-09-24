package wedoogift.level1.common.error;

public class VoucherTypeUnknownException extends RuntimeException{

    public VoucherTypeUnknownException(String code){
        super("Voucher type with code " + code + " is unknown");
    }
}
