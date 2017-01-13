package com.betterjr.common.security;
import java.util.Random;

public class PasswordCreate {
    /**
     * 获得密码
     * @param len 密码长度
     * @return
     */
    public String createPassWord(final int len){
        final int random = this.createRandomInt();
        return this.createPassWord(random, len);
    }

    public String createPassWord(final int random,final int len){
        final Random rd = new Random(random);
        final int  maxNum = 62;
        final StringBuffer sb = new StringBuffer();
        int rdGet;//取得随机数
        final char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
                'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', 'A','B','C','D','E','F','G','H','J','K',
                'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y' ,'Z', '2', '3', '4', '5', '6', '7', '8', '9' };//排除辩识度较差字符

        int count=0;
        while(count < len){
            rdGet = Math.abs(rd.nextInt(maxNum));//生成的数最大为62-1
            if (rdGet >= 0 && rdGet < str.length) {
                sb.append(str[rdGet]);
                count ++;
            }
        }
        return sb.toString();
    }

    public int createRandomInt(){
        //得到0.0到1.0之间的数字，并扩大100000倍
        double temp = Math.random()*100000;
        //如果数据等于100000，则减少1
        if(temp>=100000){
            temp = 99999;
        }
        final int tempint = (int)Math.ceil(temp);
        return tempint;
    }

    public static void main(final String[] args){
        final PasswordCreate pwc = new PasswordCreate();
        System.out.println(pwc.createPassWord(8));
    }
}