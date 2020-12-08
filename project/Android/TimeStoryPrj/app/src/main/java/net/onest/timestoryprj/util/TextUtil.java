package net.onest.timestoryprj.util;

import android.widget.TextView;

public class TextUtil {
    private static TextView textView;
    private static String s;
    private static int length;
    private static long time;
    private static int n = 0;
    private static int nn;
    private boolean flag = false;

    public TextUtil(TextView textView, String s, long time) {
        this.textView = textView;
        this.s = s;
        this.time = time;
        this.length = s.length();
        // 开启线程
        startTextView(n);
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private void startTextView(int n) {
        new Thread() {
            @Override
            public void run() {
                String stv = s.substring(0, n);
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!flag) {
                            textView.setText(stv);
                        } else {
                            textView.setText(s);
                        }
                    }
                });
                try {
                    if (!flag) {
                        Thread.sleep(time);
                        nn = n + 1;
                        if (nn <= length) {
                            startTextView(nn);
                        }
                    } else {
                        this.interrupt();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
