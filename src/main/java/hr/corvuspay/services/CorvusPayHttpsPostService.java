package hr.corvuspay.services;

import hr.corvuspay.types.CorvusPayRequestFieldType;
import hr.corvuspay.types.CorvusPayResponseType;

import java.util.Map;

public interface CorvusPayHttpsPostService {

    Map<CorvusPayResponseType, String> executeHttpsPost(String url, Map<CorvusPayRequestFieldType, String> arguments);
}
