package com.bonnyfone.vectalign;

import java.io.File;

/**
 * Created by ziby on 20/03/16.
 */
public class AnimatedVectorDrawableUtils {

    private static String RESULT_DIR = "vectalign_export";
    private static String VECTOR_DRAWABLE_NAME = "vectalign_vector_drawable";
    private static String ANIMATED_VECTOR_DRAWABLE_NAME = "vectalign_animated_vector_drawable";
    private static String MORPH_ANIMATOR_NAME = "vectalign_morph_animator";

    private static String TEMPLATE_VECTOR_DRAWABLE =
            "<vector xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "     android:height=\"%ddp\"\n" +
            "     android:width=\"%ddp\"\n" +
            "     android:viewportHeight=\"%d\"\n" +
            "     android:viewportWidth=\"%d\" >\n" +
            "     <path\n" +
            "         android:name=\"v\"\n" +
            "         android:strokeColor=\"%s\"\n" +
            "         android:strokeWidth=\"%d\"\n" +
            "         android:fillColor=\"%s\"\n" +
            "         android:pathData=\"%s\" />\n" +
            " </vector>";

    private static String TEMPLATE_ANIMATED_VECTOR_DRAWABLE =
            " <animated-vector xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "   android:drawable=\"@drawable/%s\" >\n" +
            "     <target\n" +
            "         android:name=\"v\"\n" +
            "         android:animation=\"@anim/%s\" />\n" +
            " </animated-vector>";

    private static String TEMPLATE_MORPH_ANIMATOR =
            " <set xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
            "     <objectAnimator\n" +
            "         android:duration=\"2000\"\n" +
            "         android:propertyName=\"pathData\"\n" +
            "         android:valueFrom=\"%s\"\n" +
            "         android:valueTo=\"%s\"\n" +
            "         android:valueType=\"pathType\"/>\n" +
            " </set>";

    public static boolean export(File destinationDir, String[] solution, boolean stroke, boolean fill, String strokeColor, int strokeWidth, String fillColor, int width, int height, int viewportWidth, int viewportHeight){
        try{
            String basePath = destinationDir.getAbsolutePath()+"/"+RESULT_DIR;
            File baseDir = new File(basePath);
            if(baseDir.exists()){
                int i=1;
                while((baseDir = new File(basePath +"_"+i)).exists()){
                    i++;
                }
            }
            baseDir.mkdirs();
            File drawableDir = new File(baseDir.getAbsolutePath() + "/drawable");
            File animDir = new File(baseDir.getAbsolutePath() + "/anim");
            drawableDir.mkdirs();
            animDir.mkdirs();

            for(int j=0; j<solution.length; j++){
                int fromIndex = j;
                int toIndex  = (j+1)%2;
                String fromString = fromIndex == 0 ? "start" : "end";
                String toString = toIndex == 1 ? "end" : "start";
                String genVDName = VECTOR_DRAWABLE_NAME + "_" + fromString;
                String genMAName = MORPH_ANIMATOR_NAME + "_" + fromString + "_to_" + toString;
                String getAVDName = ANIMATED_VECTOR_DRAWABLE_NAME + "_" + fromString + "_to_" + toString;
                String vectorDrawable = String.format(TEMPLATE_VECTOR_DRAWABLE, height, width, viewportHeight, viewportWidth, strokeColor, stroke ? strokeWidth : 0, fill ? fillColor : "#00000000", solution[fromIndex]);
                String animatedVectorDrawable = String.format(TEMPLATE_ANIMATED_VECTOR_DRAWABLE, genVDName, genMAName);
                String morphAnimator = String.format(TEMPLATE_MORPH_ANIMATOR, solution[fromIndex], solution[toIndex]);

                Utils.writeToFile(drawableDir.getAbsolutePath() + "/" + genVDName + ".xml", vectorDrawable);
                Utils.writeToFile(drawableDir.getAbsolutePath()+"/"+ getAVDName + ".xml", animatedVectorDrawable);
                Utils.writeToFile(animDir.getAbsolutePath()+"/"+ genMAName + ".xml", morphAnimator);
            }

            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
