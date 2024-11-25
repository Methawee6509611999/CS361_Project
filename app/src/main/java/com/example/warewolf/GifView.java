package com.example.warewolf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

public class GifView extends View {
    private Movie movie;
    private long movieStart;
    private Matrix matrix;

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix(); // สร้าง Matrix เพื่อจัดการการปรับขนาด
    }

    // กำหนดให้ GIF ใช้ไฟล์จาก resources
    public void setGifResource(int gifResource) {
        InputStream inputStream = getResources().openRawResource(gifResource);
        movie = Movie.decodeStream(inputStream);
        invalidate();
    }

    // เมื่อขนาดของ View เปลี่ยน (เช่น ขนาดหน้าจอ) กำหนดขนาดใหม่ให้ GIF
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (movie != null) {
            // คำนวณขนาดใหม่ของ GIF เพื่อให้พอดีกับ View
            float scaleX = (float) w / movie.width();
            float scaleY = (float) h / movie.height();
            matrix.setScale(scaleX, scaleY);  // ปรับขนาด GIF ตามอัตราส่วนของ View
        }
    }

    // วาด GIF บน Canvas
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (movie != null) {
            long now = SystemClock.uptimeMillis();
            if (movieStart == 0) {
                movieStart = now;
            }
            int duration = movie.duration() == 0 ? 1000 : movie.duration();
            int relTime = (int) ((now - movieStart) % duration);
            movie.setTime(relTime);

            // วาด GIF ลงใน canvas โดยใช้ Matrix ที่ปรับขนาดแล้ว
            canvas.save();
            canvas.concat(matrix);  // ใช้ Matrix ในการปรับขนาด
            movie.draw(canvas, 0, 0);
            canvas.restore();

            invalidate();
        }
    }
}
