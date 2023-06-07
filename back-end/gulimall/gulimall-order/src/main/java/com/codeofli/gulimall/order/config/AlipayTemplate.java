package com.codeofli.gulimall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.codeofli.gulimall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "9021000122682477";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDiKpLJeqihKJrma+BQ7MciDQzhPiCM+FI6hQM8WQP9F05G5NZBeW129m5GM0X0/1/HS9xP++9Bl58jN+PviJZx35Vga350VOaQJWWoPIUa5FWv3lQruf/cmhWIiWmWxSe6y7OUqOwixfIDMp4dw3zm+e/MH+6BeUAmLMyN6MkYJmdsIj1jCQ22Mpmciy8PoVh9Z4DSr3GvcM331ykbtab9oxlBKGZvTs4kblCP4vU62dwygm2Ym8WHm5FKngR1UUY5pZQzBKyT+B39RmsnGkOOu3WF4qK/Zojbjhx45iHNQtQeLH7/I8Yjb+g9wtyqHUe/PPzssYDsQRCg4W3PJ4oFAgMBAAECggEBANDYi2+t6qXW/yMkHqHb5A7Xijs8yYYjkJslVNWXRY2ixc/FZOxVGvYtnasJkeIpJ+K7dJgPhuJFBtjYtYztzi8dTa0Z1k3KUJZvUi5GpIsuqsmaPpOaUGD/kD7i1TQ6nvNpQ01Vp6h8rXyyczv81wvbZMpT+kRpGQmneMTaX0gCAjSLncmsUsIVC9tNngZ7zLhbAzSUdnHFqiOOhxjAzM3a+ZH00guRSrOvYdg/yf72kz20NHNVoWZ7PNOgm770QC2L91Kt6MuebEcNl7eqjr5kJiS0mkOqmdaRu+il1MNkH45DfIeABRJMYxPJtP7InL2+uumQoF5m7J4ZsD4i7UECgYEA87r+nEHQrOuz2DAvtYD3knwHZihtq61gG+P+FFtY7qsrhdTdnibqz1fUi/2rXS8OWYaVIqGAaaaR4DCJLYg2ZEHs8Ge8/4y9pXfpaompjGWIL0oCgA2XOxSIY+OQSgiARDTl5217WDe+7D0V7Mi/BD5aXizeGR0Ao5almv5oK50CgYEA7Y052mrbr2+oZbqo2NhaYqwjm0E5SrtuBX/S6SZtdv0uCHEc4qBhZZiadINR9IW458eErj+tza+6g81Dq4BZE/XcAArYbN4MCF105kOqvi3Q2n81NUfbnZqS4zARFH/3MrYE1ddM/PUeT5kZ76Zb/WiOQFTJwtRbMKM6c5QiD4kCgYBJ2QGmNKbWjgAK4+bAttfMcXoIcP1xw/FX4getAVR71C82uJ2BescwlasX039ll4lghM+RE6AF4utWIz6dJiVc0UDKQpRYVbtsdF2r4xQVLvpJYcJKLFQDGaCNq3ifhd3fTKvQKDL0MsfNamDHQrflwEK/QBb3QvtblZyNzsGfXQKBgFaseLdg1dck/mRfvsuf5RqrhDJZkfXafUI0aVf4VWhc6DbcTpXBvse67mAkqfp18Alhmt+ZDGlH/N2qP06L/fRyeFrrmhveyLUkpEp/1gSd4t5WG6vdFBxlEcMi/phkOGWgVBNSAx0osm6IhhYStO25AG4ogZU8tikA5YrWObdBAoGAEM3LymtuN/zg1D0JNJQPtfWgbalcM8cKaibliHjdSJs716ukzk9tFZluqUa/pFDEWUCXKgkYjy3nAPkrH+NRubZvP14j/t1yOIIK/mf/kRtLDXn+6rbUKI+IpR4xc9EJo7JkkJohGoA5rfwzluKjf8f4cRYaewqeWJXERGHi9iU=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiRF/M4i3C607UiBl9Ml9iHZsx8E5SuNaBN7KT0jSuTXqN32eaJcDZeLpKQa2CZM8lIHHqMsAX4E00LhYf/Atf1/G/uop+Bd6Z8fixAASZ71/tOZoPwpAZUHePv1B0Lv6WyjU0CLg4C+k0vwhVTu0OHxTpkDiWWRQzVvywkk46e2dk9lmNv6zNOJ25gCHwbecgNr/74Eb35nleARlSSxQEabiOtGwwKb/yataU13SstS+Gs2z3rDbL/CEPwLptUO2zTPejhg3pg2L+7bFV3W/gNKkdP/2949bSCK/tAgyniAZa4JUzeB6NaVGRDaQyX9N71al7F15rISvYWqAt7ISAQIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url="http://75p034j007.goho.co:40971/payed/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url="http://order.gulimall.com/memberOrder.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";


    //订单超时时间
    private String timeout = "1m";
    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+timeout+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
