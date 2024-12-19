package wallet;

import java.util.*;

import static util.Const.*;

public class CreditCardValidation {
  public static boolean validation(String card_string) {
    long credit_number = 0;
    try { credit_number = Long.parseLong(card_string); }
    catch (NumberFormatException e) { return false; }

    return  (sizeCheck(credit_number)>= 13 && sizeCheck(credit_number) <= 16 &&
        (prefixCheck(credit_number, prefix_VISA) || prefixCheck(credit_number, prefix_MASTER) ||
            prefixCheck(credit_number, prefix_AMERICAN_EXPRESS) || prefixCheck(credit_number, prefix_DISCOVER)));
  }
  private static int sizeCheck(long c_num) {
    String num = c_num+"";
    return num.length();
  }
  private static boolean prefixCheck(long c_num, int d) {
    return getPrefix(c_num, sizeCheck(d)) == d;
  }

  public static int prefixCheck(String card_string){
    long credit_number = 0;
    try { credit_number = Long.parseLong(card_string); }
    catch (NumberFormatException e) { return 0; }

    int[] prefix = {prefix_VISA,
                    prefix_MASTER,
                    prefix_DISCOVER,
                    prefix_AMERICAN_EXPRESS};

    for(int i = 0; i < 4; i++){
      if(prefixCheck(credit_number, prefix[i]))
        return prefix[i];
    }
    return 0;
  }
  private static long getPrefix(long c_num, int k) {
    if(sizeCheck(c_num)>k) {
      String num = c_num + "";
      return Long.parseLong(num.substring(0, k));
    }
    return c_num;
  }
}