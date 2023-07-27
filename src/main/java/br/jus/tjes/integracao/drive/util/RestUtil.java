package br.jus.tjes.integracao.drive.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class RestUtil {

	public static <T> T convertEntityToObject(HttpEntity entity, Class<T> classe) throws IOException {
		String result = EntityUtils.toString(entity);
		T objectResult = convertJsonToObject(result, classe);
		return objectResult;
	}

	public static <T> T convertJsonToObject(String result, Class<T> classe) {
		return new Gson().fromJson(result, classe);
	}
}
