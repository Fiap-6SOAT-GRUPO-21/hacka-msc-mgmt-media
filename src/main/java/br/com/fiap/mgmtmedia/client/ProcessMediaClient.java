package br.com.fiap.mgmtmedia.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "process-media")
public interface ProcessMediaClient {

    // TODO: send media to process

}
