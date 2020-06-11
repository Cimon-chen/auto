package cn.auto.core.data;

import cn.auto.core.utils.CoreConfig;
import cn.auto.core.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chenmeng on 2016/8/19.
 */
public class DataService {
    private ExcelUtils eu;
    private Row labelRow;

    public DataService(String dataPath) {
        File dataFile = new File(dataPath);
        if (dataFile.exists()) {
            eu = new ExcelUtils(dataPath);
        } else {
            eu = new ExcelUtils(CoreConfig.TEST_DATA_DIR + dataPath);
        }
        eu.setStartReadPos(1);
        eu.setOnlyReadOneSheet(true);
    }

    public DataService(String dir, String fileName) {
        eu = new ExcelUtils(dir + fileName);
        eu.setStartReadPos(1);
        eu.setOnlyReadOneSheet(true);
    }

    /**
     * 根据excel的sheet名称将数据读入到bean对象。要求Bean对象的变量名与excel中sheet的label名一致。
     *
     * @param sheetName
     * @param beanClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> getSheetBean(String sheetName, Class<T> beanClass) throws Exception {
        return getSheetBean(sheetName, null, beanClass);
    }


    public <T> Iterator<Object[]> getSheetIterator(String sheetName, Class<T> beanClass) throws Exception {
        List<Object[]> data = new ArrayList<Object[]>();
        List<T> dataBeanList = getSheetBean(sheetName, beanClass);
        for (Object o : dataBeanList) {
            data.add(new Object[]{o});
        }
        return data.iterator();
    }

    /**
     * 根据excel的sheet名称将指定行数据读入到bean对象。要求Bean对象的变量名与excel中sheet的label名一致。
     *
     * @param sheetName
     * @param ids
     * @param beanClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> getSheetBean(String sheetName, int[] ids, Class<T> beanClass) throws Exception {
        List<T> list = new ArrayList();
        List<Row> rows = null;
        if (ids != null && ids.length > 0) {
            rows = getSheetListByIds(sheetName, ids);
        } else {
            rows = getSheetList(sheetName);
        }
        for (Row row : rows) {
            Object bean = beanClass.newInstance();
            bind(bean, row);
            list.add((T) bean);
        }
        return list;
    }

    /**
     * 将sheet读取为二维数组
     *
     * @param sheetName
     * @return
     * @throws Exception
     */
    public String[][] getSheetArray(String sheetName) throws Exception {
        eu.setSelectedSheetName(sheetName);
        return eu.readExcelArray();
    }

    /**
     * 将excel行数据 通过set方法设置到bean对象
     *
     * @param bean
     * @param row
     * @throws Exception
     */
    private void bind(Object bean, Row row) throws Exception {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method m : methods) {
            String name = m.getName();
            if (name.startsWith("set")) {
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    if (name.substring(3).toLowerCase().equals(getCellLabel(row.getCell(i).getColumnIndex()))) {
                        m.invoke(bean, new Object[]{getCellValue(row.getCell(i))});
                        break;
                    }
                }
            }
        }
    }

    /**
     * 获取Cell 值
     *
     * @param cell
     * @return
     * @throws Exception
     */
    private String getCellValue(Cell cell) throws Exception {
        return eu.getCellValue(cell);
    }

    /**
     * 根据行号读取Sheet行到list
     *
     * @param sheetName
     * @param ids
     * @return
     * @throws Exception
     */
    private List<Row> getSheetListByIds(String sheetName, int[] ids) throws Exception {
        setLabel(sheetName);
        return eu.getRowsByIndex(ids);
    }

    private List<Row> getSheetList(String sheetName) throws Exception {
        setLabel(sheetName);
        return eu.readExcel();
    }

    private void setLabel(String sheetName) throws Exception {
        eu.setSelectedSheetName(sheetName);
        eu.setStartReadPos(0);
        labelRow = eu.readExcel().get(0);
        eu.setStartReadPos(1);
    }

    private String getCellLabel(int no) throws Exception {
        return eu.getCellValue(labelRow.getCell(no)).toLowerCase();
    }

}
