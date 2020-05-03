package com.LidarVisualTool.app.repository;

import com.LidarVisualTool.app.model.SelectedDataPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectedItemDataPartRepository extends JpaRepository<SelectedDataPart, Long> {

    @Query(value = "SELECT r.raw_selected_data " +
            "FROM selected_data_parts r" +
            "         join raw_data_store rds on r.raw_data_id = rds.id " +
            "where rds.main_menu_id = ?1" +
            "  AND rds.data_id = ?2", nativeQuery = true)
    List<String> getAllSelectedDataPartsForMenuIdAndDataPartId(Long menuId, Long dataId);

//    @Query(value="SELECT r FROM selected_data_parts r WHERE r.selected_item_name_id = :id")
//    List<SelectedDataPart> getAllBySelectedItemName(@Param("id") Long selectedItemNameId);

    @Query(value="SELECT * FROM selected_data_parts r WHERE r.selected_item_name_id = ?1", nativeQuery = true)
    List<SelectedDataPart> getAllBySelectedItemName(Long selectedItemNameId);
}
