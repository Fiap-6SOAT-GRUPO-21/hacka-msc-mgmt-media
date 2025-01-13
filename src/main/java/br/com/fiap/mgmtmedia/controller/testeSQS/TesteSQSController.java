package br.com.fiap.mgmtmedia.controller.testeSQS;

import br.com.fiap.mgmtmedia.sqs.testerSQS.TesteSQS;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste-sqs")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class TesteSQSController {

    @Autowired
    private TesteSQS testeSQS;

    @PostMapping("/publicar")
    public String teste() {
        testeSQS.publishMessage("Meu primeiro teste de SQS");
        return "DEU CERTO";
    }
}
