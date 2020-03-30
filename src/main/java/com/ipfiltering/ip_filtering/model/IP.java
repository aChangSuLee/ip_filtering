package com.ipfiltering.ip_filtering.model;

import lombok.Getter;
import sun.net.util.IPAddressUtil;

import java.math.BigInteger;

@Getter
public class IP {
    private boolean is_ipv4;
    private int subnet;
    private String ip_string;
    private Number ip_num;

    public IP (String ip) {
        this.is_ipv4 = isIpv4(ip);
        this.subnet = getSubnet(ip);
        this.ip_string = getIpString(ip);
        if (this.is_ipv4) {
            this.ip_string = applySubnetting(this.subnet);
        }
        this.ip_num = ipToNum(this.ip_string);
    }

    private boolean isIpv4(String ip) {
        return ip.contains(".");
    }

    private int getSubnet(String ip) {
        if (this.is_ipv4) {
            if (ip.contains("/")) {
                return Integer.parseInt(ip.substring(ip.indexOf("/") + 1));
            }
        } else {
            return -1;
        }
        return 32;
    }

    private String getIpString(String ip) {
        String _ip = ip;
        if (ip.contains("/")) {
            _ip = ip.substring(0, ip.indexOf("/"));
        }
        return _ip;
    }

    private Number ipToNum(String ip) {
        Number num_of_ip;
        if (is_ipv4) {
            long ipv4_num = 0;
            for (int i = 0; i < 4; i++) {
                ipv4_num += Long.parseLong(ip.split("\\.")[i]) * Math.pow(256, 3 - i);
            }
            num_of_ip = ipv4_num;
        } else {
            num_of_ip = new BigInteger(IPAddressUtil.textToNumericFormatV6(ip));
        }
        return num_of_ip;
    }

    private String applySubnetting(int subnet) {
        int _subnet = subnet;
        int[] subnet_mask = new int[4];
        for (int i = 0; i < 4; i++) {
            subnet_mask[i] = (int) (256 - Math.pow(2, 8 - Math.max(Math.min(8, _subnet), 0)));
            _subnet -= 8;
        }

        String[] ip_part = this.ip_string.split("\\.");
        String[] subnetting_ip = new String[4];

        for (int i = 0; i < 4; i++) {
            subnetting_ip[i] = Integer.toString(Integer.parseInt(ip_part[i]) & subnet_mask[i]);
        }

        return String.join(".", subnetting_ip);
    }

    public Number getSubnetIP(int subnet) {
        String subnet_ip = applySubnetting(subnet);
        return ipToNum(subnet_ip);
    }
}
