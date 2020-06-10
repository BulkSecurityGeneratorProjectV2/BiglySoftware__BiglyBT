/*
 * Created on Jan 19, 2007
 * Created by Paul Gardner
 * Copyright (C) Azureus Software, Inc, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */


package com.biglybt.core.peermanager.messaging.azureus;

import java.util.HashMap;
import java.util.Map;

import com.biglybt.core.peermanager.messaging.Message;
import com.biglybt.core.peermanager.messaging.MessageException;
import com.biglybt.core.peermanager.messaging.MessagingUtil;
import com.biglybt.core.util.DirectByteBuffer;

public class
AZStatReply
	implements AZMessage
{
	private final byte version;
	private DirectByteBuffer buffer = null;

	private final Map	reply;

	public
	AZStatReply(
		Map		_reply,
		byte	_version )
	{
		reply		= _reply;
		version		= _version;
	}

	@Override
	public String
	getID()
	{
		return( AZMessage.ID_AZ_STAT_REPLY );
	}

	@Override
	public byte[]
	getIDBytes()
	{
		return( AZMessage.ID_AZ_STAT_REPLY_BYTES );
	}

	@Override
	public String
	getFeatureID()
	{
		return( AZMessage.AZ_FEATURE_ID );
	}

	@Override
	public int
	getFeatureSubID()
	{
		return( AZMessage.SUBID_AZ_STAT_REPLY );
	}

	@Override
	public int
	getType()
	{
		return( Message.TYPE_PROTOCOL_PAYLOAD );
	}

	@Override
	public byte getVersion() { return version; }

	@Override
	public String
	getDescription()
	{
		return( getID() + ": " + reply );
	}

	public Map
	getReply()
	{
		return( reply );
	}

	@Override
	public DirectByteBuffer[]
	getData()
	{
		if ( buffer == null ){

			Map	map = new HashMap();

			map.put( "reply", reply );

			buffer = MessagingUtil.convertPayloadToBencodedByteStream( map, DirectByteBuffer.AL_MSG );
		}

		return new DirectByteBuffer[]{ buffer };
	}

	@Override
	public Message
	deserialize(
		DirectByteBuffer 	data,
		byte				version )

		throws MessageException
	{
		Map payload = MessagingUtil.convertBencodedByteStreamToPayload( data, 1, getID() );

		Map reply	= (Map)payload.get( "reply" );

		return( new AZStatReply( reply, version ));
	}

	@Override
	public void
	destroy()
	{
		if ( buffer != null ){

			buffer.returnToPool();
		}
	}
}