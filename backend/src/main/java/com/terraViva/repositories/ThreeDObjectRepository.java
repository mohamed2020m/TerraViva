package com.terraViva.repositories;

import com.terraViva.models.Note;
import com.terraViva.models.Professor;
import com.terraViva.models.ThreeDObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThreeDObjectRepository extends JpaRepository<ThreeDObject, Long> {
    Optional<ThreeDObject> findByName(String name);
    List<ThreeDObject> findByProfessor(Professor professor);

    @Query("SELECT COUNT(o.name) " +
            "FROM ThreeDObject o " +
            "WHERE o.professor.id = :professorId")
    Long threeDObjectCountByProfessorId (Long professorId);

    @Query("SELECT c.name, COUNT(obj) " +
            "FROM ThreeDObject obj JOIN obj.category c " +
            "WHERE c.parentCategory IS NOT NULL " +
            "AND obj.professor.id = :professorId " +
            "GROUP BY c.name " +
            "ORDER BY COUNT(obj) DESC")
    List<Object[]> findThreeDObjectCountsByProfessorSubCategories(@Param("professorId") Long professorId);

    @Query("SELECT COUNT(o) FROM ThreeDObject o WHERE o.category.id = :categoryId")
    Long countByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT o FROM ThreeDObject o WHERE o.category.id = :categoryId")
    List<ThreeDObject> getThreeDObjectBySubCategory(Long categoryId);

    List<ThreeDObject> findTop5ByOrderByCreatedAtDesc();

    @Query("SELECT n FROM Note n WHERE n.threeDObject.id = :threeDObjectId AND n.student.id = :studentId")
    List<Note> getNotesByThreeDObjects(
        @Param("studentId") Long studentId,
        @Param("threeDObjectId") Long threeDObjectId
    );
}
