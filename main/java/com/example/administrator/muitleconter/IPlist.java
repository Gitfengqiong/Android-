package com.example.administrator.muitleconter;

public  class  IPlist {
    public final String  m_istart ="efef" ; //开始字头 0xef 0xfe
    public String m_cVersion; //版本号
    public String m_cType; //数据包类型 =0x01 查询 =0x02 设置 =0x10 应答
    public String my_ethernet_address; //MACunsigned char my_ip_address[4]; //IP
    public String multicast_ip_address; //MULTICAST
    public String host_ip_address; //HOSTIP
    public String gateway_ip_address; //GATEWAY
    public String mask_ip_address; //MASK
    public String m_LocalPort; //LOCAL PORT
    public String m_HostPort; //HOST PORT
    public String m_NetMode; //net working mode
    public String m_CommBaud; //buad
    public String m_ID; //ID
    public String DEVICE_name ; //device name
    public String DECICE_model; //device model
    public String m_IPMode; //ip_mode
    public String password ; //login password
    public String identify ; //产品识别码
    public String dns_ip ; //dns
    public String my_ip_address;
}
