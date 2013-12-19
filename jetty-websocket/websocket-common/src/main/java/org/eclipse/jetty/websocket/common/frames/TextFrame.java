//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.websocket.common.frames;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.websocket.common.OpCode;
import org.eclipse.jetty.websocket.common.WebSocketFrame;

public class TextFrame extends DataFrame
{
    String text;
    
    public TextFrame()
    {
        super(OpCode.TEXT);
    }

    @Override
    public Type getType()
    {
        return Type.TEXT;
    }

    public TextFrame setPayload(String str)
    {
        /* delay wrapping string until needed */
        super.setPayload(null);
        text=str;
        return this;
    }
    
    
    @Override
    public WebSocketFrame setPayload(ByteBuffer buf)
    {
        text=BufferUtil.toString(buf,StandardCharsets.UTF_8);
        return super.setPayload(buf);
    }

    @Override
    public ByteBuffer getPayload()
    {
        ByteBuffer data=super.getPayload();
        if (data==null && text!=null)
            super.setPayload(data=ByteBuffer.wrap(StringUtil.getUtf8Bytes(text)));
        return data;
    }

    @Override
    public int getPayloadLength()
    {
        getPayload();
        return super.getPayloadLength();
    }

    @Override
    public void releaseBuffer()
    {
        if (super.getPayload()!=null)
            super.releaseBuffer();
        text=null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof TextFrame)
        {
            TextFrame other = (TextFrame)obj;
            if (text==null)
                return other.text==null;
            return text.equals(other.text);
        }
        getPayload();
        return super.equals(obj);
    }

    @Override
    public boolean hasPayload()
    {
        return text!=null;
    }

    @Override
    public void reset()
    {
        text=null;
        super.reset();
    }

    @Override
    public String getPayloadAsUTF8()
    {
        return text;
    }
}
