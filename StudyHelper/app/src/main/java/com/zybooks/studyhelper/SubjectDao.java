package com.zybooks.studyhelper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface SubjectDao {
    @Query("SELECT * FROM Subject WHERE id = :id")
    public Subject getSubject(long id);

    @Query("SELECT * FROM Subject WHERE text = :subjectText")
    public Subject getSubjectByText(String subjectText);

    @Query("SELECT * FROM Subject ORDER BY text")
    public List<Subject> getSubjects();

    @Query("SELECT * FROM Subject ORDER BY updated DESC")
    public List<Subject> getSubjectsNewerFirst();

    @Query("SELECT * FROM Subject ORDER BY updated ASC")
    public List<Subject> getSubjectsOlderFirst();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertSubject(Subject subject);

    @Update
    public void updateSubject(Subject subject);

    @Delete
    public void deleteSubject(Subject subject);
}