package com.example.regenerationoil;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JobDao {
    @Insert
    long insertJob(JobEntity job);

    @Query("SELECT * FROM jobs WHERE id = :jobId")
    JobEntity getJobById(int jobId);

    @Query("SELECT * FROM jobs")
    List<JobEntity> getAllJobs();
}
