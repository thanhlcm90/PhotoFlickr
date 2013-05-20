#include <jni.h>

void toRGB565(unsigned short *yuvs, int widthIn, int heightIn,
		unsigned int *rgbs, int widthOut, int heightOut) {
	int half_widthIn = widthIn >> 1;

	//the end of the luminance data
	int lumEnd = (widthIn * heightIn) >> 1;
	//points to the next luminance value pair
	int lumPtr = 0;
	//points to the next chromiance value pair
	int chrPtr = lumEnd;
	//the end of the current luminance scanline
	int lineEnd = half_widthIn;

	int x, y;
	for (y = 0; y < heightIn; y++) {
		int yPosOut = (y * widthOut) >> 1;
		for (x = 0; x < half_widthIn; x++) {
			//read the luminance and chromiance values
			int Y1 = yuvs[lumPtr++];
			int Y2 = (Y1 >> 8) & 0xff;
			Y1 = Y1 & 0xff;
			int Cr = yuvs[chrPtr++];
			int Cb = ((Cr >> 8) & 0xff) - 128;
			Cr = (Cr & 0xff) - 128;

			int R, G, B;
			//generate first RGB components
			B = Y1 + ((454 * Cb) >> 8);
			if (B < 0)
				B = 0;
			if (B > 255)
				B = 255;
			G = Y1 - ((88 * Cb + 183 * Cr) >> 8);
			if (G < 0)
				G = 0;
			if (G > 255)
				G = 255;
			R = Y1 + ((359 * Cr) >> 8);
			if (R < 0)
				R = 0;
			if (R > 255)
				R = 255;
			int val = ((R & 0xf8) << 8) | ((G & 0xfc) << 3) | (B >> 3);

			//generate second RGB components
			B = Y1 + ((454 * Cb) >> 8);
			if (B < 0)
				B = 0;
			if (B > 255)
				B = 255;
			G = Y1 - ((88 * Cb + 183 * Cr) >> 8);
			if (G < 0)
				G = 0;
			if (G > 255)
				G = 255;
			R = Y1 + ((359 * Cr) >> 8);
			if (R < 0)
				R = 0;
			if (R > 255)
				R = 255;
			rgbs[yPosOut + x] =
					val
							| ((((R & 0xf8) << 8) | ((G & 0xfc) << 3) | (B >> 3))
									<< 16);
		}
		//skip back to the start of the chromiance values when necessary
		chrPtr = lumEnd + ((lumPtr >> 1) / half_widthIn) * half_widthIn;
		lineEnd += half_widthIn;
	}
}

JNIEXPORT void JNICALL Java_de_offis_magic_core_NativeWrapper_image2TextureColor
  (JNIEnv *env, jclass clazz,
  jbyteArray imageIn, jint widthIn, jint heightIn,
  jobject imageOut, jint widthOut, jint heightOut,
  jint filter) {

	jbyte *cImageIn = env->GetByteArrayElements(imageIn, 0);
	jbyte *cImageOut = (jbyte*)env->GetDirectBufferAddress(imageOut);


	toRGB565((unsigned short*)cImageIn, widthIn, heightIn, (unsigned int*)cImageOut, widthOut, heightOut);

	env->ReleaseByteArrayElements(imageIn, cImageIn, JNI_ABORT);
}
