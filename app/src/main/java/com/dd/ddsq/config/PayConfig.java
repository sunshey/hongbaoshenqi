package com.dd.ddsq.config;

/**
 * Created by admin on 2017/2/28.
 */

public class PayConfig {
    /**
     * appid		-- 应用appid
     * privateKey   -- 应用私钥
     * publicKey	-- 平台公钥(platKey)
     * */


    /**
     * 线上环境
     */
    public static final String appid = "3010531216";
    public static final String privateKey = "MIICWwIBAAKBgQCnF5TSOBG7hBYC+WQ0jk+bTB9ld1ozWSVRSrsxl67gX1Z60QXIeQ7Mq8M5yMZ7BBiiiS5ZGf3KjQ9k1JzXlelV58rqa4kn8MZCsWsHU6KSPtmGJEKNGM5JkchRI9cgPWgjP8X1oPyHN/IB2eqiHhYxSVLt2ZQNIjartW2nQWRUfwIDAQABAoGAMvVjIrWM2uZOnDuYztpz9sXBcX1z2eMfGWq0Kky7PZx10GD85YXd+JeNo4rS1WOLa85wfyYJG4PnpPWem7URgu3glvyz5+/eVFyLjLpJrV8e0aMWSc0lGseZeH4hyzyLt3dUX4PhCt//um6zbxg43VLWAhtXDca5rhBqpkmsE/kCQQDhO5JiEZaCBYCodtlZ2bpDlH1RMi43rKuNlAEKEgNdzsRoArPiVI0n2OYmtfG9GYRNdQ3q29jWg3MjJUmNWccVAkEAverSm+Fh3Yqjy87aRoBQgq1FkBCGl61BLDOYLZ6EPr1s/tSGQNU0xpVX61VncJnTroAKq18OjWnr8B7BbL7SQwJAGdc62TOJYAt/klRoZW2ceCpuIkWulcaivBCR+fTNHBMf54r/1mS6+SpiZWiUfoyR6E0YtqxYeWwFZq/de3EkbQJADnCnWNDXNxgOgjWVJR0mgfkYu+51vVygAvNdU8KkvXx7qBiSqKVNYPgD+lHBDq+zh6YWqZcNrTTripNcE5+irwJAQvaWwbEmPNGak2MY5V5DY8eLX6PGk1TUW1NL5A15wdH2K1P1xxMmgkyzbbAGtRcMPuEpj3zlQV1/NDTXamfxFg==";
    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMdJgWVcklxt7i5W6yjBKqjK6kH9sAfz6OUs2u9UVyEjbY3esDXXb3YJpDQnRianeFJ+g9a93Cby24tPzcI0KnyfDgdkPjGdNQmFOxch32Rw0WEOlVk/5exVJTx7NKu1MJqqyZaTTlG2RfARWdaKIV/9gjtn0mOWmUMSZYmm0u9QIDAQAB";


    public static final int PAY_TYPE_CONSUME = 0;//支付类型 0 消费
    public static final int PAY_TYPE_EXCEPTIONAL = 1;//支付类型 1打赏

    public static final String PAY_WAY_IAPPPAY = "iapppay";//支付方式 iapppay支付
    public static final String PAY_WAY_ALIPAY = "alipay";//支付方式 alipay支付
    public static final String PAY_WAY_NOWPAY_WX = "ipaynow_wxpay";//现在支付 微信支付
    public static final String PAY_WAY_NOWPAY_ALI = "ipaynow_alipay";//现在支付 支付宝支付
    public static final String PAY_WAY_XJHY_WXPAY = "xjhy_wxpay";//香蕉互娱 微信支付
    public static final String PAY_WAY_WXPAY = "wxpay";//微信支付
    public static final String PAY_WAY_H5WXPAY = "ipaynowh5";//H5微信支付
    public static final String PAY_WAY_XXPAY = "xxpay";//小小贝微信支付
    public static final String PAY_WAY_SPWXPAY = "spwxpay";//扬扬微信支付


    public static final String NOWPAY_WX = 13 + "";//现在支付微信支付
    public static final String NOWPAY_ALI = 12 + "";//现在支付支付宝支付

    public static final String Prf_VipInfo = "pay_success";
    public static final String Prf_VipInfos = "Prf_VipInfos";

    /**
     * 支付宝支付(机游)
     */
    public static final String ALIPAY_APPUID = "2088121204955234";
    public static final String PARTNERAPPID = "2017021505682466";
    public static String PRIVATEKEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKrVLyye4EpL0rznUbS7HoY3n3w30lOUOEOIFr0DtUkEXl71kykHdzeYPyZ/CxUWir25FCL3W4eO8t/ef49hJbdWyHjZ64i2eC3Zpm8/TdUOAWJCFxLFFDYhhtyGvFiRYgYMzvLk9BA9rXOnVgnYuf5tpC5eA7EWBFxKdWli6fi7AgMBAAECgYAkeESM6xII6mz/5QY9ZiEbGC7VDvm43BCy4g+wpGUERIl9DkIvZSefV8JLjp2oD9roq+VPna0NoIfbIR3sVxn80yzZ0TsFxda/UPGvujIl+5J5oEQajQxivSN+ITTN8qr8bGGlK+m+z9U02qLjnNZT288vhFmUCVpRkZoDhyqfmQJBAOdFaqlTmVn+/n2SyRsEi5U7voaWbYJuvPC6Rw5ivrXsWAYWp0si0rc+R+FKA52muTxjb+o21hiZfDN6b/pRUJcCQQC9GWM8KRIOR1uuix6F4yWv4CrT0yb35wAkqee9t4mVCK4XvDzJuL67ON6rwhkVCTGl72MsuyeaSyYiS7yoXDl9AkEAqeBAb647h3KZZo3KcLEPAohG879NIzDKUhzSEswPf7viEF7VdYhXtUfnLqHaNBQCHkyAKU8sRhd/tqRp2jmecwJBAIPraidWH5qGfZdOxoRw6qG/qSENdWoMZTUustTPbdPislEsqNuxXp4OFomMZyMdvt0cG/vgtMgXFQtn+r0ZWaECQGiFArZntnRHpQXnAUyhQIEhLSIw2kAeYd7yDKtOjpY6hlXDjAWKbO1mnhUjuH+rgv7iKJbYD5A+ZqnV9sy6KMA=\n" +
            "-----END PUBLIC KEY-----";
    public static final String EMAIL = "jy@5577.com";
    public static final String NOTIFY_URL = "http://apps.289.com/api/index/notify_url/alipay";

    /**
     * 支付宝（扬程）
     */
    public static final String NEW_PRIVATEKEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ8xt5V5sDJ/iJLlx54/ZC2f70ZZaq6+OWJ2j1MP5wPTFQl8jhTvazlYG12OYb2Os1wz9ge3o2yqWuCez0iBcsNmDQv5gL/g5SpEHMdtWJjZToAJIhfShA+3KhqIQProt9tVFNdzyeCumDlNru/E4i6vMGMQOXvVswnVD6xzDbyFAgMBAAECgYBrCsAZxxg97FlGBTGDa7aNoyP91rQQSaJGXdgjjubUQIO9vD3BP/eGOIM2Fj4Mer6Jufqt18IQmD/BF6fjZf9m5xvMh1fyR29y/qc1EkhKBSIJmjMzXeZuFxSywQXEKR3cUilWGn6Ah0ZyL2FQRZbCZvDm3FzbwPRtMBtKn836pQJBAOvPjgPAN9QMJ5D6x7HaM2nmEChNHSVUNdCVFvAnUH++PJom9cOMXu8Gu5Dcg3yHi/2u/PisIP8qtCjErIG01vsCQQCs0uYNAoiTnT9WRPeJ099/nZyFiJhCjUtdndnbDj5SfinOOVlhES50keghejjLiNNap3T32i1DzhwZcpYOMWJ/AkBuiNwI5hFFVG03iFYTCDURbsYkHgGLfe272Cboa4VQW9wMZjbNcv8NDBrfzH3V4bO9NkleR1EwStQoSAiUxDABAkBtpfDnWDAm16GTrnYPMCmg4fKolh1kPBkQ8FGENek8lWcYvDIE7821j8zjUJQwLkNKROxlRDtrxZz2WEj0AEkpAkAZcH4lD9k61Rhl1Lqux2QhvyJZdsLu1YXNiwrSEJc7et/WlZcr7xXUA0hddKmG5uw3xwgRrUYHrrNrWH2rk/J4";
    public static final String NEW_PARTNERID = "2088621460504790";
    public static final String NEW_ALIPAY_APPUID = "2017032006298951";
    public static final String NEW_EMAIL = "948874017@qq.com";
    public static final String NEW_NOTIFY_URL = "http://u.xcjz8.com/api/index/notify_url/alipay";

}
