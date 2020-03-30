package com.ipfiltering.ip_filtering.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;

@Component
public class BlackListFilter {
    private HashMap<Integer, HashSet<Long>> ipv4_block_list = new HashMap<>();
    private HashSet<BigInteger> ipv6_block_list = new HashSet<>();
    private ArrayList<Integer> subnet_range;

    @Autowired
    public BlackListFilter(@Value("${ip_black_list}") ArrayList<String> original_ip_data) {
        HashSet<Integer> _subnet_range = new HashSet<>();

        original_ip_data.forEach( (original_ip) -> {
            IP ip = new IP(original_ip);
            if (ip.is_ipv4()) {
                int subnet = ip.getSubnet();
                if (null == ipv4_block_list.get(subnet)) {
                    this.ipv4_block_list.put(subnet, new HashSet<>());
                }
                this.ipv4_block_list.get(subnet).add((Long) ip.getIp_num());
                _subnet_range.add(subnet);
            } else {
                this.ipv6_block_list.add((BigInteger) ip.getIp_num());
            }
        });

        this.subnet_range = new ArrayList(_subnet_range);
        Collections.sort(this.subnet_range);
    }

    public ApiResponseMessage filterBlackList(String _ip) {
        IP ip = new IP(_ip);
        if (ip.is_ipv4()) {
            for (Integer subnet : subnet_range) {
                if (ipv4_block_list.get(subnet).contains(ip.getSubnetIP(subnet))) {
                    return new ApiResponseMessage("Deny");
                }
            }
        } else {
            if (ipv6_block_list.contains(ip.getIp_num())) {
                return new ApiResponseMessage("Deny");
            }
        }
        return new ApiResponseMessage("Allow");
    }
}
