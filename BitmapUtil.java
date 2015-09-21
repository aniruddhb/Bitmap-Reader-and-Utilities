import gpdraw.*;
import java.util.*;
import java.io.*;
import java.awt.*;

public class BitmapUtil {
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
    
    // 3D Array for BMP Data and for CHAR MSG
    int[][][] bitmapData;
    String[] msgData;
    
    // length of Message
    short charArrayCounter = 0;
    
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
            charArrayCounter = appSpecOne;
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
    
    public void encodeBMP(String txtFileName){
        try {
            FileReader FR = new FileReader(new File(txtFileName));
            BufferedReader BR = new BufferedReader(FR);
            int sizeOfArray = 0;
            while (BR.ready()){
                sizeOfArray++;
                BR.read();
            }
            FileReader FRnew = new FileReader(new File(txtFileName));
            BufferedReader BRnew = new BufferedReader(FRnew);
            msgData = new String[sizeOfArray];
            int counter = 0;
            while (BRnew.ready()){
                msgData[counter] = addZeroes(Integer.toBinaryString((int)BRnew.read()));
                counter++;
            }
        } catch (IOException io){
            // do nothing
        }
        int pixelsWrittenOn = 0;
        for (int outer = 0; outer < heightOfBitmap; outer++){
            for (int inner = 0; inner < widthOfBitmap; inner++){
                if (charArrayCounter == msgData.length){
                    break;
                }
                int red = bitmapData[inner][outer][0];   
                int newRed = 0;
                if (msgData[charArrayCounter].charAt(pixelsWrittenOn) != (addZeroes(Integer.toBinaryString(bitmapData[inner][outer][0]))).charAt(7)){
                    newRed = red ^ 1;
                } else {
                    newRed = red;
                }
                bitmapData[inner][outer][0] = newRed;
                pixelsWrittenOn++;
                if (pixelsWrittenOn % 8 == 0){
                    charArrayCounter++;
                    pixelsWrittenOn = 0;
                }
                if (charArrayCounter == msgData.length){
                    break;
                }
                int green = bitmapData[inner][outer][1];
                int newGreen = 0;
                if (msgData[charArrayCounter].charAt(pixelsWrittenOn) != (addZeroes(Integer.toBinaryString(bitmapData[inner][outer][1]))).charAt(7)){
                    newGreen = green ^ 1;
                } else {
                    newGreen = green;
                }
                bitmapData[inner][outer][1] = newGreen;
                pixelsWrittenOn++;
                if (pixelsWrittenOn % 8 == 0){
                    charArrayCounter++;
                    pixelsWrittenOn = 0;
                }
                if (charArrayCounter == msgData.length){
                    break;
                }
                int blue = bitmapData[inner][outer][2];
                int newBlue = 0;
                if (msgData[charArrayCounter].charAt(pixelsWrittenOn) != (addZeroes(Integer.toBinaryString(bitmapData[inner][outer][2]))).charAt(7)){
                    newBlue = blue ^ 1;
                } else {
                    newBlue = blue;
                }
                bitmapData[inner][outer][2] = newBlue;
                pixelsWrittenOn++;
                if (pixelsWrittenOn % 8 == 0){
                    charArrayCounter++;
                    pixelsWrittenOn = 0;
                }
                if (charArrayCounter == msgData.length){
                    break;
                }
            }
        }
    }
    
    public void extractMessage(String txtFileName){
        String binaryChar = "";
        int charArrayCounter = 0;
        for (int outer = 0; outer < heightOfBitmap; outer++){
            for (int inner = 0; inner < widthOfBitmap; inner++){
                if (charArrayCounter == this.charArrayCounter){
                    break;
                }
                String red = Integer.toBinaryString(bitmapData[inner][outer][0]);
                red = red.substring(red.length() - 1);
                binaryChar += red;
                if (binaryChar.length() == 8){
                    String charToAdd = (char)(Integer.parseInt(binaryChar, 2)) + "";
                    try {
                        FileWriter FW = new FileWriter(txtFileName, true);
                        FW.write(charToAdd, 0, charToAdd.length());
                        FW.close();
                    } catch (IOException io){
                        // do nothing
                    }
                    binaryChar = "";
                    charArrayCounter++;
                }
                if (charArrayCounter == this.charArrayCounter){
                    break;
                }
                String green = Integer.toBinaryString(bitmapData[inner][outer][1]);
                green = green.substring(green.length() - 1);
                binaryChar += green;
                if (binaryChar.length() == 8){
                    String charToAdd = (char)(Integer.parseInt(binaryChar, 2)) + "";
                    try {
                        FileWriter FW = new FileWriter(txtFileName, true);
                        FW.write(charToAdd, 0, charToAdd.length());
                        FW.close();
                    } catch (IOException io){
                        // do nothing
                    }
                    binaryChar = "";
                    charArrayCounter++;
                }
                if (charArrayCounter == this.charArrayCounter){
                    break;
                }
                String blue = Integer.toBinaryString(bitmapData[inner][outer][2]);
                blue = blue.substring(blue.length() - 1);
                binaryChar += blue;
                if (binaryChar.length() == 8){
                    String charToAdd = (char)(Integer.parseInt(binaryChar, 2)) + "";
                    try {
                        FileWriter FW = new FileWriter(txtFileName, true);
                        FW.write(charToAdd, 0, charToAdd.length());
                        FW.close();
                    } catch (IOException io){
                        // do nothing
                    }
                    binaryChar = "";
                    charArrayCounter++;
                }
                if (charArrayCounter == this.charArrayCounter){
                    break;
                }
            }
        }
    }
    
    public void saveBMP(String bmpFileName){
        try {
            FileOutputStream fOutStream = new FileOutputStream(bmpFileName);
            DataOutputStream dOutStream = new DataOutputStream(fOutStream);
            dOutStream.writeByte((byte)idField.charAt(0));
            dOutStream.writeByte((byte)idField.charAt(1));
            dOutStream.writeInt(Integer.reverseBytes(sizeOfBMP));
            dOutStream.writeShort(Short.reverseBytes(charArrayCounter));
            dOutStream.writeShort(Short.reverseBytes(charArrayCounter));
            dOutStream.writeInt(Integer.reverseBytes(pixelArrayOffset));
            dOutStream.writeInt(Integer.reverseBytes(bytesInDIBHeader));
            dOutStream.writeInt(Integer.reverseBytes(widthOfBitmap));
            dOutStream.writeInt(Integer.reverseBytes(heightOfBitmap));
            dOutStream.writeShort(Short.reverseBytes(planesUsed));
            dOutStream.writeShort(Short.reverseBytes(bitsPerPixel));
            dOutStream.writeInt(Integer.reverseBytes(pixelArrayCompression));
            dOutStream.writeInt(Integer.reverseBytes(rawDataSize));
            dOutStream.writeInt(Integer.reverseBytes(horizontalResolution));
            dOutStream.writeInt(Integer.reverseBytes(verticalResolution));
            dOutStream.writeInt(Integer.reverseBytes(numberOfColors));
            dOutStream.writeInt(Integer.reverseBytes(colorImportance));
            for (int outer = 0; outer < heightOfBitmap; outer++){
                int paddingToAdd = 0;
                for (int inner = 0; inner < widthOfBitmap; inner++){
                    int red = bitmapData[inner][outer][0];
                    int green = bitmapData[inner][outer][1];
                    int blue = bitmapData[inner][outer][2];
                    dOutStream.writeByte(blue);
                    dOutStream.writeByte(green);
                    dOutStream.writeByte(red);
                    paddingToAdd = (inner + 1) * 3;
                }        
                if (paddingToAdd % 4 != 0){
                    paddingToAdd = 4 - (paddingToAdd % 4);
                    for (int writePadding = 0; writePadding < paddingToAdd; writePadding++){
                        dOutStream.writeByte(0);
                    }
                }
            }
        } catch (IOException io){
            // do nothing
        }
    }
    
    public String addZeroes(String shortBin){
        int stringLength = shortBin.length();
        for (int addZeroes = stringLength; addZeroes < 8; addZeroes++){
            shortBin = "0" + shortBin;
        }
        return shortBin;
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
        pad = new SketchPad((int)(1.25 * widthOfBitmap), (int)(1.25 * heightOfBitmap), 0);
        pen = new DrawingTool(pad);
        bitmapData = new int[widthOfBitmap][heightOfBitmap][3];
    }
    
    public void printBMPHeader(){
        System.out.println("         BMP HEADER         ");
        System.out.println("ID Field : " + idField);
        System.out.println("BMP File Size : " + sizeOfBMP);
        System.out.println("Application Specific (One) (for this lab, I made this variable represent the length of the message) : " + appSpecOne);
        System.out.println("Application Specific (Two) (for this lab, I made this variable represent the length of the message) : " + appSpecTwo);
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
        // BitmapUtil BU = new BitmapUtil();
        // BU.loadBMP("doge_meme.bmp");
        // BU.encodeBMP("BitmapUtil.txt");
        // BU.extractMessage("reflection_extracted.txt");
        // BU.saveBMP("BitmapUtil.bmp");
        // BU.printBMPHeader();
    }
}
