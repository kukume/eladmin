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
package me.zhengjie.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.domain.vo.TradeVo;
import me.zhengjie.domain.AlipayConfig;
import me.zhengjie.utils.AliPayStatusEnum;
import me.zhengjie.utils.AlipayUtils;
import me.zhengjie.service.AliPayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aliPay")
public class AliPayController {

    private final AlipayUtils alipayUtils;
    private final AliPayService alipayService;

    @GetMapping
    public ResponseEntity<AlipayConfig> queryAliConfig() {
        return new ResponseEntity<>(alipayService.find(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateAliPayConfig(@Validated @RequestBody AlipayConfig alipayConfig) {
        alipayService.config(alipayConfig);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/toPayAsPC")
    public ResponseEntity<String> toPayAsPc(@Validated @RequestBody TradeVo trade) throws Exception {
        AlipayConfig aliPay = alipayService.find();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        String payUrl = alipayService.toPayAsPc(aliPay, trade);
        return ResponseEntity.ok(payUrl);
    }

    @PostMapping(value = "/toPayAsWeb")
    public ResponseEntity<String> toPayAsWeb(@Validated @RequestBody TradeVo trade) throws Exception {
        AlipayConfig alipay = alipayService.find();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        String payUrl = alipayService.toPayAsWeb(alipay, trade);
        return ResponseEntity.ok(payUrl);
    }

    @AnonymousGetMapping("/return")
    public ResponseEntity<String> returnPage(HttpServletRequest request, HttpServletResponse response) {
        AlipayConfig alipay = alipayService.find();
        response.setContentType("text/html;charset=" + alipay.getCharset());
        //内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipay)) {
            //商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            System.out.println("商户订单号" + outTradeNo + "  " + "第三方交易号" + tradeNo);

            // 根据业务需要返回数据，这里统一返回OK
            return new ResponseEntity<>("payment successful", HttpStatus.OK);
        } else {
            // 根据业务需要返回数据
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/notify")
    @AnonymousAccess
    public ResponseEntity<Object> notify(HttpServletRequest request) {
        AlipayConfig alipay = alipayService.find();
        Map<String, String[]> parameterMap = request.getParameterMap();
        //内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipay)) {
            //交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //付款金额
            String totalAmount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //验证
            if (tradeStatus.equals(AliPayStatusEnum.SUCCESS.getValue()) || tradeStatus.equals(AliPayStatusEnum.FINISHED.getValue())) {
                // 验证通过后应该根据业务需要处理订单
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
