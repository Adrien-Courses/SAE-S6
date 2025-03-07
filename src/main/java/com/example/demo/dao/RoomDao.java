package com.example.demo.dao;

import com.example.demo.model.Room;

public interface RoomDao extends GenericDao<Room, Long> {
    Room findByRoomNumber(String roomNumber);
} 