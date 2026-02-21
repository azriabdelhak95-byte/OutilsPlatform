package com.example.outilsPlatform.repository;

import com.example.outilsPlatform.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
