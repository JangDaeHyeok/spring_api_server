package jdh.test.api;

import java.security.KeyPair;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import jdh.test.api.util.encrypt.CipherUtil;
import jdh.test.api.util.encrypt.KeyComponent;

@Component
public class ApiInitializer implements ApplicationRunner{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired SqlSessionFactory sf;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.error("[프로젝트 기동] RSA 키 생성");
		KeyPair keys = CipherUtil.genRSAKeyPair();
		KeyComponent.setPrivateKey(keys.getPrivate());
		KeyComponent.setPublicKey(keys.getPublic());
		log.error("[프로젝트 기동]      RSA Public Key :" +  CipherUtil.publicKeyToStr(keys.getPublic()));
	}
}
