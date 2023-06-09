package com.example.bootIML.jcodec.javase.api.awt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.example.bootIML.jcodec.api.SequenceEncoder;
import com.example.bootIML.jcodec.common.Codec;
import com.example.bootIML.jcodec.common.Format;
import com.example.bootIML.jcodec.common.io.NIOUtils;
import com.example.bootIML.jcodec.common.io.SeekableByteChannel;
import com.example.bootIML.jcodec.common.model.Rational;
import com.example.bootIML.jcodec.javase.scale.AWTUtil;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 *
 */
public class AWTSequenceEncoder extends SequenceEncoder {

    public AWTSequenceEncoder(SeekableByteChannel out, Rational fps) throws IOException {
        super(out, fps, Format.MOV, Codec.H264, null);
    }

    public static AWTSequenceEncoder createSequenceEncoder(File out, int fps) throws IOException {
        return new AWTSequenceEncoder(NIOUtils.writableChannel(out), Rational.R(fps, 1));
    }

    public static AWTSequenceEncoder create25Fps(File out) throws IOException {
        return new AWTSequenceEncoder(NIOUtils.writableChannel(out), Rational.R(25, 1));
    }

    public static AWTSequenceEncoder create30Fps(File out) throws IOException {
        return new AWTSequenceEncoder(NIOUtils.writableChannel(out), Rational.R(30, 1));
    }

    public static AWTSequenceEncoder create2997Fps(File out) throws IOException {
        return new AWTSequenceEncoder(NIOUtils.writableChannel(out), Rational.R(30000, 1001));
    }

    public static AWTSequenceEncoder create24Fps(File out) throws IOException {
        return new AWTSequenceEncoder(NIOUtils.writableChannel(out), Rational.R(24, 1));
    }

    public void encodeImage(BufferedImage bi) throws IOException {
        encodeNativeFrame(AWTUtil.fromBufferedImageRGB(bi));
    }
}
