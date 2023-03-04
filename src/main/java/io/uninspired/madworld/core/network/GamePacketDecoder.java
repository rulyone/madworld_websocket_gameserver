// /**
//  * Copyright (c) 2023, Pablo Martinez
//  * All rights reserved.
//  * 
//  * This source code is licensed under the BSD-style license found in the
//  * LICENSE file in the root directory of this source tree.  
//  */

// package io.uninspired.madworld.core.network;

// import io.netty.buffer.ByteBuf;
// import io.netty.channel.ChannelHandlerContext;
// import io.netty.channel.socket.DatagramPacket;
// import io.netty.handler.codec.MessageToMessageDecoder;
// import io.uninspired.madworld.core.GameCommand;
// import io.uninspired.madworld.core.commands.JoinCommand;
// import io.uninspired.madworld.core.commands.TeleportCommand;

// import java.awt.Point;
// import java.nio.charset.Charset;
// import java.util.List;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// /**
//  *
//  * @author rulyone
//  */
// public class GamePacketDecoder extends MessageToMessageDecoder<DatagramPacket> {
    
//     private Logger log = LoggerFactory.getLogger(GamePacketDecoder.class);

//     @Override
//     protected GameCommand decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
//         ByteBuf data = datagramPacket.content();
//         log.debug("decoding datagrampacket... readablebytes: " + data.readableBytes());
//         if (data.readableBytes() < 6) {
//             return InvalidPacketException("Need");;
//         }
//         short packetType = data.readShort();
//         int packetSize = data.readInt();
//         if (data.readableBytes() != packetSize) {
//             throw new InvalidPacketException();
//         }
//         GameCommand gameCommand = decodePacket(packetType, packetSize, data);
//         out.add(gameCommand);
//     }
    
//     private GameCommand decodePacket(short packetType, int packetSize, ByteBuf data) {
//         log.debug("Received packetType " + packetType + " with payload size of " + packetSize + " bytes");
//         if (packetType == PacketTypes.MOVE_PACKET.getPacketType()) {
//             return decodeMovePacket(packetSize, data);
//         }else if(packetType == PacketTypes.JOIN_PACKET.getPacketType()) {
//             return decodeJoinPacket(packetSize, data);
//         }else{
//             //unknown
//             log.error("Unknown packet arrived to gamepacketdecoder: " + packetType);
//             return null;
//         }
//     }
    
//     private TeleportCommand decodeMovePacket(int packetSize, ByteBuf data) {
//         //timestamp - playerId - payload
//         long timestamp = data.readLong();
//         long playerId = data.readLong();
//         int x = data.readInt();
//         int y = data.readInt();
//         return new TeleportCommand(timestamp, playerId, new Point(x, y));
//     }
    
//     private JoinCommand decodeJoinPacket(int packetSize, ByteBuf data) {
//         long timestamp = data.readLong();
//         long playerId = data.readLong();
//         String payload = data.readCharSequence(data.readableBytes(), Charset.forName("utf-8")).toString();
//         return new JoinCommand(timestamp, playerId, payload);
//     }
    
// }
