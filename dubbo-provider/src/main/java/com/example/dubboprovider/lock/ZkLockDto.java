package com.example.dubboprovider.lock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ZkLockDto {

    /**
     * znode 序号 字符串
     * 000000001
     */
    private String sid;
    /**
     * 序号字符串 long型
     * 000000001--->1
     */
    private Long id;

    /**
     * test-0000001---> test
     */
    private String prePath;

    /**
     * test-0000001
     */
    private String path;


}
