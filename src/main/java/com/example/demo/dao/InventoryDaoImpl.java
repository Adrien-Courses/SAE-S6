package com.example.demo.dao;

import com.example.demo.model.Inventory;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;

@Repository
public class InventoryDaoImpl extends AbstractHibernateDao<Inventory, Long> implements InventoryDao {
    
    @Override
    public Inventory findByItemCode(String itemCode) {
        return (Inventory) getEntityManager()
                .createQuery("FROM Inventory WHERE itemCode = :itemCode")
                .setParameter("itemCode", itemCode)
                .getSingleResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Inventory> findByQuantityLessThan(Integer quantity) {
        return getEntityManager()
                .createQuery("FROM Inventory WHERE quantity < :quantity")
                .setParameter("quantity", quantity)
                .getResultList();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Inventory> findNeedingRestock() {
        // Bad practice: Business logic in DAO
        return getEntityManager()
                .createQuery("FROM Inventory i WHERE i.quantity <= i.reorderLevel")
                .getResultList();
    }
    
    @Override
    public void updateStock(String itemCode, Integer quantity) {
        // Bad practice: Multiple queries in one method
        Inventory inventory = findByItemCode(itemCode);
        inventory.setQuantity(quantity);
        inventory.setLastRestocked(new Date());
        update(inventory);
        
        // Bad practice: Direct System.out in DAO
        System.out.println("Updated stock for " + itemCode + " to " + quantity);
    }
    
    @Override
    public void updatePrice(String itemCode, Double price) {
        // Bad practice: No validation
        getEntityManager()
                .createQuery("UPDATE Inventory SET unitPrice = :price WHERE itemCode = :itemCode")
                .setParameter("price", price)
                .setParameter("itemCode", itemCode)
                .executeUpdate();
    }
} 