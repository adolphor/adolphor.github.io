console.log("Joyoung DNS server start... ");
console.log("ssid: " + $network.wifi.ssid);
console.log("domain: " + $domain);

let ssid = $network.wifi.ssid;
let ipaddr = $network.v4.primaryAddress;

let joyo_dns_servers = [
    '172.31.1.125',
    '172.31.1.126'
];

let ssids = [
    'jy_m',
    'jyguest',
    'Joyoung',
    'HUAWEI-G10SE4_Wi-Fi5',
    'jymesh',
    'jymesh_5G',
    'jylink',
    'jylink_5G',
    'jysmart',
    'jysmart_5G',
    'jymi',
    'jymi_5G'
];

let reg =  /^(10\.170\.\d{1,}\.\d{1,})$/

if (ssids.includes(ssid) || reg.test(ipaddr)) {
    console.log("Joyoung DNS server: " + JSON.stringify(joyo_dns_servers));
    $done({servers: joyo_dns_servers})
} else {
    console.log("Joyoung DNS server: by surge");
    $done({})
}

