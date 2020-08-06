package com.mooc.libnetwork.interceptor;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoggingInterceptor implements Interceptor {


	private static final String TAG = "LoggingInterceptor";

	@NotNull
	@Override
	public Response intercept(@NotNull Chain chain) throws IOException {
		//这个chain里面包含了request和response，所以你要什么都可以从这里拿
		Request request = chain.request();

		long t1 = System.nanoTime();//请求发起的时间
		Log.i(TAG, String.format("发送 [%s] 方法 协议: [%s] 请求Url: %s  %s", request.method(),
				chain.connection() != null ? chain.connection().protocol() : Protocol.HTTP_1_1,
				request.url(),
				request.headers()));

		Response response = chain.proceed(request);

		long t2 = System.nanoTime();//收到响应的时间

		//这里不能直接使用response.body().string()的方式输出日志
		//因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
		//个新的response给应用层处理
		ResponseBody responseBody = response.peekBody(1024 * 2048);

		Log.i(TAG, String.format("收到响应: 状态码 [%s]:  响应时间: %.1fms%n%s", response.code(), (t2 - t1) / 1e6d, response.headers()));
		print(responseBody.string(), true);
		return response;
	}

	/**
	 * JSON字符串格式化打印输出
	 *
	 * @param shouldShowAll 是否将全部打印出来
	 */
	public static void print(String mJson, boolean shouldShowAll) {
		if (mJson == null) return;
		try {
			Log.i(TAG, "----------print begin-----------");
			String willPrintString = format(mJson);
			if (!shouldShowAll) {
				Log.i(TAG, willPrintString);
			} else {
				int length = willPrintString.length();
				int index = 0;
				while (length > 2000) {
					Log.i(TAG, willPrintString.substring(index * 2000, (index + 1) * 2000));
					length -= 2000;
					index++;
				}
				Log.i(TAG, willPrintString.substring(index * 2000));
			}
			Log.i(TAG, "----------print end-----------");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String format(String mJson) {
		StringBuilder soruce = new StringBuilder(mJson);
		if (mJson == null || mJson.equals("")) {
			return null;
		}
		int offset = 0;//目标字符串插入空格偏移量
		int bOffset = 0;//空格偏移量
		for (int i = 0; i < mJson.length(); i++) {
			char charAt = mJson.charAt(i);
			if (charAt == '{' || charAt == '[') {
				bOffset += 4;
				soruce.insert(i + offset + 1, "\n" + generateBlank(bOffset));
				offset += (bOffset + 1);
			} else if (charAt == ',') {
				soruce.insert(i + offset + 1, "\n" + generateBlank(bOffset));
				offset += (bOffset + 1);
			} else if (charAt == '}' || charAt == ']') {
				bOffset -= 4;
				soruce.insert(i + offset, "\n" + generateBlank(bOffset));
				offset += (bOffset + 1);
			}
		}
		return soruce.toString();
	}


	private static String generateBlank(int num) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < num; i++) {
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}
}
