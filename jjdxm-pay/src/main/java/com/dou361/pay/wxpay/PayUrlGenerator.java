package com.dou361.pay.wxpay;

import android.text.TextUtils;

import com.dou361.pay.ConstantKeys;
import com.dou361.pay.L;
import com.dou361.pay.PayInfo;
import com.tencent.mm.sdk.modelpay.PayReq;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 构建 支付参数，sig等  支付金额 暂时没填
 *
 * @author BaoHong.Li
 * @version V1.0
 * @date 2015-7-16 下午4:27:02
 * @update (date)
 */
public class PayUrlGenerator {

    private static final String TAG = PayUrlGenerator.class.getName();

    private PayInfo payInfo;

    private PayReq req;

    public PayUrlGenerator(PayInfo payInfo) {
        this.payInfo = payInfo;
        this.req = new PayReq();
    }

    /**
     * 构建支付参数
     *
     * @return PayReq
     */
    public PayReq genSignPayReq() {
        validatePayInfo(this.payInfo);
        req.appId = ConstantKeys.WxPay.APP_ID;
        req.partnerId = ConstantKeys.WxPay.MCH_ID;
        req.prepayId = this.payInfo.getOrderNo();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        Map<String, String> signParams = new HashMap<String, String>();
        signParams.put("appid", req.appId);
        signParams.put("noncestr", req.nonceStr);
        signParams.put("package", req.packageValue);
        signParams.put("partnerid", req.partnerId);
        signParams.put("prepayid", req.prepayId);
        signParams.put("timestamp", req.timeStamp);
        req.sign = genAppSign(signParams);
        L.d("orion", signParams.toString());
        return this.req;
    }

    private String genAppSign(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(ConstantKeys.WxPay.API_KEY);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();

        L.e(TAG, " genAppSign " + appSign);
        return appSign;
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    /**
     * 验证 支付参数的有效性
     *
     * @param payInfo
     * @return void
     * @autour BaoHong.Li
     * @date 2015-7-17 上午10:44:11
     * @update (date)
     */
    private void validatePayInfo(PayInfo payInfo) {

        if (TextUtils.isEmpty(payInfo.getOrderNo())) {
            throw new IllegalArgumentException(" payInfo.orderNo is  null !");
        }

//		if (TextUtils.isEmpty(payInfo.getBody())) {
//			throw new IllegalArgumentException(" payInfo.body is  null !");
//		}

//		if (TextUtils.isEmpty(payInfo.getSubject())) {
//			throw new IllegalArgumentException(" payInfo.subject is  null !");
//		}

//		if (TextUtils.isEmpty(payInfo.getNotifyUrl())) {
//			throw new IllegalArgumentException(" payInfo.notifyUrl is  null !");
//		}

    }

}
