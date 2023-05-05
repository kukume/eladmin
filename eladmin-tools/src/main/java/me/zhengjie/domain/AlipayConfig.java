/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.domain;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 支付宝配置类
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Data
@Entity
@Table(name = "tool_alipay_config")
public class AlipayConfig implements Serializable {

    @Id
    @Column(name = "config_id")
    private Long id;

    @NotBlank
    private String appId;

    @NotBlank
    private String privateKey;

    @NotBlank
    private String publicKey;

    private String signType="RSA2";

    @Column(name = "gateway_url")
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    private String charset= "utf-8";

    @NotBlank
    private String notifyUrl;

    @NotBlank
    private String returnUrl;

    private String format="JSON";

    @NotBlank
    private String sysServiceProviderId;

}
