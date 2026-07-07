package com.vertex.brief;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyBriefRepository extends JpaRepository<DailyBrief, LocalDate> {
}

