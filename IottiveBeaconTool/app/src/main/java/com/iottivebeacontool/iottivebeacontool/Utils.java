package com.iottivebeacontool.iottivebeacontool;


import android.content.Context;
import android.widget.Toast;

public class Utils {

    /**
     * Convert section of a byte[] into a hexadecimal string
     */
    public static String toHexString(byte[] data, int offset, int length) {
        StringBuilder sb = new StringBuilder();

        for (int i=offset; i < length; i++) {
            sb.append(String.format("%02x", data[i] & 0xFF));
        }

        return sb.toString();
    }

    public static String toHex(String arg) {
        String hex=Integer.toHexString(Integer.parseInt(arg));
        String padded = new String(new char[4 - hex.length()]).replace('\0', '0') + hex;
        return padded;
    }
    public static String toBeaconName(int i, String s) {
        StringBuilder sb = new StringBuilder();
        sb.append("beacons/");
        sb.append(i);
        sb.append("!");
        sb.append(s);

        return sb.toString();
    }
    public static String getUUid(String s){
       String str = s.replace("-", "");
       /* StringBuilder uuid_new = new StringBuilder(s);
        uuid_new.deleteCharAt(8);
        uuid_new.deleteCharAt(12);
        uuid_new.deleteCharAt(16);
        uuid_new.deleteCharAt(20);*/
        return str;
    }
    /**
     * Show a Toast message
     */
    public static void showToast(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
