package com.vertex.weekly;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WeeklySnapshotRepository extends JpaRepository<WeeklySnapshot, LocalDate> {
}

