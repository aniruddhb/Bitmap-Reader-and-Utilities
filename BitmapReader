import gpdraw.*;
import java.util.*;
import java.io.*;
import java.awt.*;

public class BitmapReader {
    // BMP and DIB Header short(s)
    String idField = "";
    short appSpecOne = 0;
    short appSpecTwo = 0;
    short planesUsed = 0;
    short bitsPerPixel = 0;
    
    // BMP and DIB Header int(s)
    int sizeOfBMP = 0;
    int pixelArrayOffset = 0;
    int bytesInDIBHeader = 0;
    int widthOfBitmap = 0;
    int heightOfBitmap = 0;
    int pixelArrayCompression = 0;
    int rawDataSize = 0;
    int horizontalResolution = 0;
    int verticalResolution = 0;
    int numberOfColors = 0;
    int colorImportance = 0;
    
    // 3D Array
    int[][][] bitmapData;
    
    // Drawing Items
    DrawingTool pen = null;
    SketchPad pad = null;
    
    public void loadBMP(String fName){
        try {
            FileInputStream fInStream = new FileInputStream(fName);
            DataInputStream dInStream = new DataInputStream(fInStream);
            idField = "";
            idField += (char)dInStream.readByte();
            idField += (char)dInStream.readByte();
            if (!idField.equals("BM")){
                System.out.println("Stop trying to mess up this program - The magic number is incorrect!");
            }
            sizeOfBMP = Integer.reverseBytes(dInStream.readInt());
            appSpecOne = Short.reverseBytes(dInStream.readShort());
            appSpecTwo = Short.reverseBytes(dInStream.readShort());
            pixelArrayOffset = Integer.reverseBytes(dInStream.readInt());
            bytesInDIBHeader = Integer.reverseBytes(dInStream.readInt());
            widthOfBitmap = Integer.reverseBytes(dInStream.readInt());
            heightOfBitmap = Integer.reverseBytes(dInStream.readInt());
            planesUsed = Short.reverseBytes(dInStream.readShort());
            bitsPerPixel = Short.reverseBytes(dInStream.readShort());
            pixelArrayCompression = Integer.reverseBytes(dInStream.readInt());
            rawDataSize = Integer.reverseBytes(dInStream.readInt());
            horizontalResolution = Integer.reverseBytes(dInStream.readInt());
            verticalResolution = Integer.reverseBytes(dInStream.readInt());
            numberOfColors = Integer.reverseBytes(dInStream.readInt());
            colorImportance = Integer.reverseBytes(dInStream.readInt());
            initializeAttributes();
            for (int outer = 0; outer < heightOfBitmap; outer++){
                int paddingToSkip = 0;
                for (int inner = 0; inner < widthOfBitmap; inner++){
                    int blue = dInStream.readUnsignedByte();
                    int green = dInStream.readUnsignedByte();
                    int red = dInStream.readUnsignedByte();
                    bitmapData[inner][outer][0] = red;
                    bitmapData[inner][outer][1] = green;
                    bitmapData[inner][outer][2] = blue;
                    paddingToSkip = (inner + 1) * 3;
                }        
                if (paddingToSkip % 4 != 0){
                    paddingToSkip = 4 - (paddingToSkip % 4);
                    dInStream.skipBytes(paddingToSkip);
                }
            }
        } catch (IOException io){
            // do nothing
        }
    }
    
    public void drawBMP(){
        pen.setDirection(0);
        int centerXPos = (widthOfBitmap / 2) * -1;
        int centerYPos = (heightOfBitmap / 2) * -1;
        for (int outer = 0; outer < widthOfBitmap; outer++){
            for (int inner = 0; inner < heightOfBitmap; inner++){
                Color thisPixel = new Color(bitmapData[outer][inner][0], bitmapData[outer][inner][1], bitmapData[outer][inner][2]);
                pen.setColor(thisPixel);
                pen.up();
                pen.move(outer + centerXPos, inner + centerYPos);
                pen.setDirection(0);
                pen.down();
                pen.fillRect(1, 1);
            }
        }
    }
    
    public void drawBMP(int xPos, int yPos){
        pen.setDirection(0);
        int centerXPos = (xPos - (widthOfBitmap / 2));
        int centerYPos = (yPos - (heightOfBitmap / 2));
        for (int outer = 0; outer < widthOfBitmap; outer++){
            for (int inner = 0; inner < heightOfBitmap; inner++){
                Color thisPixel = new Color(bitmapData[outer][inner][0], bitmapData[outer][inner][1], bitmapData[outer][inner][2]);
                pen.setColor(thisPixel);
                pen.up();
                pen.move(outer + centerXPos, inner + centerYPos);
                pen.setDirection(0);
                pen.down();
                pen.fillRect(1,1);
            }
        }
    }
    
    public void initializeAttributes(){
        pad = new SketchPad(widthOfBitmap, heightOfBitmap, 0);
        pen = new DrawingTool(pad);
        bitmapData = new int[widthOfBitmap][heightOfBitmap][3];
    }
    
    public void printBMPHeader(){
        System.out.println("BMP HEADER");
        System.out.println("ID Field : " + idField);
        System.out.println("BMP File Size : " + sizeOfBMP);
        System.out.println("Application Specific (One) : " + appSpecOne);
        System.out.println("Application Specific (Two) : " + appSpecTwo);
        System.out.println("Offset of Pixel Array : " + pixelArrayOffset);
        System.out.println("Bytes in DIB Header : " + bytesInDIBHeader);
        System.out.println("Width of bitmap in pixels : " + widthOfBitmap);
        System.out.println("Height of bitmap in pixels : " + heightOfBitmap);
        System.out.println("Number of color planes used : " + planesUsed);
        System.out.println("Number of bits per pixel : " + bitsPerPixel);
        System.out.println("Pixel Array Compression : " + pixelArrayCompression);
        System.out.println("Size of raw pixel data : " + rawDataSize);
        System.out.println("Horizontal Resolution of Image : " + horizontalResolution);
        System.out.println("Vertical Resolution of Image : " + verticalResolution);
        System.out.println("Colors in palette : " + numberOfColors);
        System.out.println("Number of Important Colors : " + colorImportance);
    }

    public static void main(String[] args){
        BitmapReader BR = new BitmapReader();
        BR.loadBMP("bitmap_size_test_1.bmp");
        BR.drawBMP();
        BR.loadBMP("bitmap_size_test_2.bmp");
        BR.drawBMP();
        BR.loadBMP("bitmap_size_test_3.bmp");
        BR.drawBMP();
        BR.loadBMP("bitmap_size_test_4.bmp");
        BR.drawBMP();
        // BR.printBMPHeader();
    }
}
