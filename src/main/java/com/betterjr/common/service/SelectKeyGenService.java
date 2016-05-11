package com.betterjr.common.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.betterjr.common.selectkey.*;

@Service
public class SelectKeyGenService {
    private static Set<ISelectKeyGenFace> keyGeneratorList = new HashSet();

    static {
        keyGeneratorList.add(new SelectKeyDateGen());
        keyGeneratorList.add(new SelectKeyTimeGen());
        keyGeneratorList.add(new SelectKeyUserIDGen());
        keyGeneratorList.add(new SelectKeyUserNameGen());
        keyGeneratorList.add(new SelectKeyclientIPGen());
        keyGeneratorList.add(new SelectKeyGuidGen());
    }

    public static Set<ISelectKeyGenFace> getKeyGeneratorList() {
        return keyGeneratorList;
    }

    public static void setKeyGeneratorList(Set<ISelectKeyGenFace> anKeyGeneratorList) {
        keyGeneratorList = anKeyGeneratorList;
    }

    public static void regKeyGen(ISelectKeyGenFace anKeyGenor) {

        keyGeneratorList.add(anKeyGenor);
    }

    public static void regKeyGenList(List<ISelectKeyGenFace> anKeyGenorList) {

        keyGeneratorList.addAll(anKeyGenorList);
    }

    public SelectKeyGenService() {
    }

    public static Object getValue(String anType, String anKey) {

        if (anType.toLowerCase().startsWith(SerialGenerator.BANK_SERIAL)) {
           anKey = anKey.substring(SerialGenerator.BANK_SERIAL.length());
           return SerialGenerator.getAppSerialNo(Integer.parseInt(anKey));
        }
        else if ("LongValue".equalsIgnoreCase(anType)) {
            return SerialGenerator.getLongValue(anKey);
        }
        else {
            for (ISelectKeyGenFace genFace : keyGeneratorList) {
                if (genFace.getName().equalsIgnoreCase(anType)) {
                    System.out.println("find " + anType);
                    return genFace.getValue(anKey);
                }
            }
        }

        return 0;
    }
}