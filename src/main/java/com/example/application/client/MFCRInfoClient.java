package com.example.application.client;

;
import cz.mfcr.wwwinfo.ares.xml_doc.schemas.ares.ares_answer.v_1_0.AresOdpovedi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${mfcr.url}", name = "mfcr", configuration = {FeignClientsConfiguration.class})
public interface MFCRInfoClient {
    @GetMapping("/cgi-bin/ares/darv_std.cgi")
    AresOdpovedi getAresResponses(@RequestParam(name = "ico") String ico);
}
