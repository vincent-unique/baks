package org.trump.vincent.utilities;

/**
 * Created by Vincent on 2017/9/5 0005.
 */

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.defaults.JavaType;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Map;

/**
 * Utility for jdbc action in accordance with java type
 */
public class JavaSQLUtils {

    private static Logger logger = LoggerFactory.getLogger(JavaSQLUtils.class);

    public static Object getValue(@NotNull ResultSet resultSet,String labelName ,String javaType){
        if (Strings.isNullOrEmpty(javaType)) {
            throw new NullPointerException("You can not get value with null java type name ["+javaType+"]");
        }
        try {
            if(javaType.equalsIgnoreCase(JavaType.Types.BOOLEAN.getTypeName())){
                return resultSet.getBoolean(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.BYTE.getTypeName())){
                return resultSet.getByte(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.BYTES.getTypeName())){
                return resultSet.getBytes(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.SHORT.getTypeName())){
                return resultSet.getShort(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.INT.getTypeName())){
                return resultSet.getInt(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.LONG.getTypeName())){
                return resultSet.getLong(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.FLOAT.getTypeName())){
                return resultSet.getFloat(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.DOUBLE.getTypeName())){
                return resultSet.getDouble(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.BIGDECIMAL.getTypeName())){
                return resultSet.getBigDecimal(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.STRING.getTypeName())){
                return resultSet.getString(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.CLOB.getTypeName())){
                return resultSet.getClob(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.NCLOB.getTypeName())){
                return resultSet.getNClob(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.BLOB.getTypeName())){
                return resultSet.getBlob(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.DATE.getTypeName())){
                return resultSet.getDate(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.TIME.getTypeName())){
                return resultSet.getTime(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.TIMESTAMP.getTypeName())){
                return resultSet.getTimestamp(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.ARRAY.getTypeName())){
                return resultSet.getArray(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.STRUCT.getTypeName())){
                return resultSet.getObject(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.REF.getTypeName())){
                return resultSet.getRef(labelName);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.URL.getTypeName())){
                return resultSet.getURL(labelName);
            }else {
                return resultSet.getObject(labelName);
            }

        }catch (SQLException sqle){
            logger.error("Exception occurs in building parameter for PrepareStatement from ResultSet.",sqle);
        }
        return null;
    }


    /****************** Java Type style********************
     * Various Driver sql types are mapped to Format Java type
     */
    public static void setValue2Parameter(@NotNull ResultSet resultSet, @NotNull PreparedStatement statement, String labelName, Integer paraIndex, String javaType) {
        if (Strings.isNullOrEmpty(javaType)) {
            throw new NullPointerException("You can not set paramater with null java type name ["+javaType+"]");
        }
        try {
           if(javaType.equalsIgnoreCase(JavaType.Types.BOOLEAN.getTypeName())){
               Boolean value = resultSet.getBoolean(labelName);
               statement.setBoolean(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.BYTE.getTypeName())){
               byte value = resultSet.getByte(labelName);
               statement.setByte(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.BYTES.getTypeName())){
               byte[] value = resultSet.getBytes(labelName);
               statement.setBytes(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.SHORT.getTypeName())){
               short value = resultSet.getShort(labelName);
               statement.setShort(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.INT.getTypeName())){
               int value = resultSet.getInt(labelName);
               statement.setInt(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.LONG.getTypeName())){
               long value = resultSet.getLong(labelName);
               statement.setLong(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.FLOAT.getTypeName())){
               float value = resultSet.getFloat(labelName);
               statement.setFloat(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.DOUBLE.getTypeName())){
               double value = resultSet.getDouble(labelName);
               statement.setDouble(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.BIGDECIMAL.getTypeName())){
               BigDecimal value = resultSet.getBigDecimal(labelName);
               statement.setBigDecimal(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.STRING.getTypeName())){
               String value = resultSet.getString(labelName);
               statement.setString(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.CLOB.getTypeName())){
               Clob value = resultSet.getClob(labelName);
//               statement.setClob(paraIndex,value);
               if(value == null){
                   statement.setNull(paraIndex, Types.CLOB);
               }else {
                   statement.setString(paraIndex, value.getSubString(1, (int) value.length()));
               }
           }else if(javaType.equalsIgnoreCase(JavaType.Types.NCLOB.getTypeName())){
               NClob value = resultSet.getNClob(labelName);
//               statement.setNClob(paraIndex,value);
               if(value == null){
                   statement.setNull(paraIndex,Types.NCLOB);
               }else {
                   statement.setString(paraIndex, value.getSubString(1, (int) value.length()));
               }
           }else if(javaType.equalsIgnoreCase(JavaType.Types.BLOB.getTypeName())){
               Blob value = resultSet.getBlob(labelName);
//               statement.setBlob(paraIndex,value);
               statement.setBytes(paraIndex,value.getBytes(1,(int)(value.length())));
           }else if(javaType.equalsIgnoreCase(JavaType.Types.DATE.getTypeName())){
               Date value = resultSet.getDate(labelName);
               statement.setDate(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.TIME.getTypeName())){
               Time value = resultSet.getTime(labelName);
               statement.setTime(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.TIMESTAMP.getTypeName())){
               Timestamp value = resultSet.getTimestamp(labelName);
               statement.setTimestamp(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.ARRAY.getTypeName())){
               Array value = resultSet.getArray(labelName);
               statement.setArray(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.STRUCT.getTypeName())){
               Object value = resultSet.getObject(labelName);
               statement.setObject(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.REF.getTypeName())){
               Ref value = resultSet.getRef(labelName);
               statement.setRef(paraIndex,value);
           }else if(javaType.equalsIgnoreCase(JavaType.Types.URL.getTypeName())){
               URL value = resultSet.getURL(labelName);
               statement.setURL(paraIndex,value);
           }else {
               statement.setObject(paraIndex,resultSet.getObject(labelName));
           }

        }catch (SQLException sqle){
            logger.error("Exception occurs in building parameter for PrepareStatement from ResultSet.",sqle);
        }
    }

    public static PreparedStatement setDefaultValue4NotNull(@NotNull PreparedStatement statement, @NotNull Integer paraIndex,@NotNull String javaType){

        if(javaType==null){
            throw new NullPointerException("None java type mapping for [java.sql.Types: "+javaType+" ]" );
        }
        try {
            if(javaType.equalsIgnoreCase(JavaType.Types.BOOLEAN.getTypeName())){
                statement.setBoolean(paraIndex,false);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.BYTE.getTypeName())){
                statement.setByte(paraIndex,Byte.valueOf("0"));
            }else if(javaType.equalsIgnoreCase(JavaType.Types.BYTES.getTypeName())){
                statement.setBytes(paraIndex,new byte[]{Byte.valueOf("0")});
            }else if(javaType.equalsIgnoreCase(JavaType.Types.SHORT.getTypeName())){
                statement.setShort(paraIndex,Short.valueOf("0"));
            }else if(javaType.equalsIgnoreCase(JavaType.Types.INT.getTypeName())){
                statement.setInt(paraIndex,0);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.LONG.getTypeName())){
                statement.setLong(paraIndex,0);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.FLOAT.getTypeName())){
                statement.setFloat(paraIndex,0);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.DOUBLE.getTypeName())){
                statement.setDouble(paraIndex,0);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.BIGDECIMAL.getTypeName())){
                statement.setBigDecimal(paraIndex,BigDecimal.ONE);
            }else if(javaType.equalsIgnoreCase(JavaType.Types.STRING.getTypeName())){
                statement.setString(paraIndex,"_");
            }else if(javaType.equalsIgnoreCase(JavaType.Types.CLOB.getTypeName())){
               /* statement.setClob(paraIndex, new Clob() {
                    @Override
                    public long length() throws SQLException {
                        return 0;
                    }

                    @Override
                    public String getSubString(long pos, int length) throws SQLException {
                        return null;
                    }

                    @Override
                    public Reader getCharacterStream() throws SQLException {
                        return null;
                    }

                    @Override
                    public InputStream getAsciiStream() throws SQLException {
                        return null;
                    }

                    @Override
                    public long position(String searchstr, long start) throws SQLException {
                        return 0;
                    }

                    @Override
                    public long position(Clob searchstr, long start) throws SQLException {
                        return 0;
                    }

                    @Override
                    public int setString(long pos, String str) throws SQLException {
                        return 0;
                    }

                    @Override
                    public int setString(long pos, String str, int offset, int len) throws SQLException {
                        return 0;
                    }

                    @Override
                    public OutputStream setAsciiStream(long pos) throws SQLException {
                        return null;
                    }

                    @Override
                    public Writer setCharacterStream(long pos) throws SQLException {
                        return null;
                    }

                    @Override
                    public void truncate(long len) throws SQLException {

                    }

                    @Override
                    public void free() throws SQLException {

                    }

                    @Override
                    public Reader getCharacterStream(long pos, long length) throws SQLException {
                        return null;
                    }
                });*/
               statement.setString(paraIndex,"_");
            }else if(javaType.equalsIgnoreCase(JavaType.Types.NCLOB.getTypeName())){
                /*statement.setNClob(paraIndex, new NClob() {
                    @Override
                    public long length() throws SQLException {
                        return 0;
                    }

                    @Override
                    public String getSubString(long pos, int length) throws SQLException {
                        return null;
                    }

                    @Override
                    public Reader getCharacterStream() throws SQLException {
                        return null;
                    }

                    @Override
                    public InputStream getAsciiStream() throws SQLException {
                        return null;
                    }

                    @Override
                    public long position(String searchstr, long start) throws SQLException {
                        return 0;
                    }

                    @Override
                    public long position(Clob searchstr, long start) throws SQLException {
                        return 0;
                    }

                    @Override
                    public int setString(long pos, String str) throws SQLException {
                        return 0;
                    }

                    @Override
                    public int setString(long pos, String str, int offset, int len) throws SQLException {
                        return 0;
                    }

                    @Override
                    public OutputStream setAsciiStream(long pos) throws SQLException {
                        return null;
                    }

                    @Override
                    public Writer setCharacterStream(long pos) throws SQLException {
                        return null;
                    }

                    @Override
                    public void truncate(long len) throws SQLException {

                    }

                    @Override
                    public void free() throws SQLException {

                    }

                    @Override
                    public Reader getCharacterStream(long pos, long length) throws SQLException {
                        return null;
                    }
                });*/
                statement.setString(paraIndex,"_");
            }else if(javaType.equalsIgnoreCase(JavaType.Types.BLOB.getTypeName())){
               /* statement.setBlob(paraIndex, new Blob() {
                    @Override
                    public long length() throws SQLException {
                        return 0;
                    }

                    @Override
                    public byte[] getBytes(long pos, int length) throws SQLException {
                        return new byte[0];
                    }

                    @Override
                    public InputStream getBinaryStream() throws SQLException {
                        return null;
                    }

                    @Override
                    public long position(byte[] pattern, long start) throws SQLException {
                        return 0;
                    }

                    @Override
                    public long position(Blob pattern, long start) throws SQLException {
                        return 0;
                    }

                    @Override
                    public int setBytes(long pos, byte[] bytes) throws SQLException {
                        return 0;
                    }

                    @Override
                    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
                        return 0;
                    }

                    @Override
                    public OutputStream setBinaryStream(long pos) throws SQLException {
                        return null;
                    }

                    @Override
                    public void truncate(long len) throws SQLException {

                    }

                    @Override
                    public void free() throws SQLException {

                    }

                    @Override
                    public InputStream getBinaryStream(long pos, long length) throws SQLException {
                        return null;
                    }
                });*/
                statement.setString(paraIndex,"_");

            }else if(javaType.equalsIgnoreCase(JavaType.Types.DATE.getTypeName())){
                statement.setDate(paraIndex,new Date(System.currentTimeMillis()));
            }else if(javaType.equalsIgnoreCase(JavaType.Types.TIME.getTypeName())){
                statement.setTime(paraIndex,new Time(System.currentTimeMillis()));
            }else if(javaType.equalsIgnoreCase(JavaType.Types.TIMESTAMP.getTypeName())){
                statement.setTimestamp(paraIndex,new Timestamp(System.currentTimeMillis()));
            }else if(javaType.equalsIgnoreCase(JavaType.Types.ARRAY.getTypeName())){
                statement.setArray(paraIndex, new Array() {
                    @Override
                    public String getBaseTypeName() throws SQLException {
                        return null;
                    }

                    @Override
                    public int getBaseType() throws SQLException {
                        return 0;
                    }

                    @Override
                    public Object getArray() throws SQLException {
                        return null;
                    }

                    @Override
                    public Object getArray(Map<String, Class<?>> map) throws SQLException {
                        return null;
                    }

                    @Override
                    public Object getArray(long index, int count) throws SQLException {
                        return null;
                    }

                    @Override
                    public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
                        return null;
                    }

                    @Override
                    public ResultSet getResultSet() throws SQLException {
                        return null;
                    }

                    @Override
                    public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
                        return null;
                    }

                    @Override
                    public ResultSet getResultSet(long index, int count) throws SQLException {
                        return null;
                    }

                    @Override
                    public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
                        return null;
                    }

                    @Override
                    public void free() throws SQLException {

                    }
                });
            }else if(javaType.equalsIgnoreCase(JavaType.Types.STRUCT.getTypeName())){
                statement.setObject(paraIndex,new Object());
            }else if(javaType.equalsIgnoreCase(JavaType.Types.REF.getTypeName())){
                statement.setRef(paraIndex, new Ref() {
                    @Override
                    public String getBaseTypeName() throws SQLException {
                        return null;
                    }

                    @Override
                    public Object getObject(Map<String, Class<?>> map) throws SQLException {
                        return null;
                    }

                    @Override
                    public Object getObject() throws SQLException {
                        return null;
                    }

                    @Override
                    public void setObject(Object value) throws SQLException {

                    }
                });
            }else if(javaType.equalsIgnoreCase(JavaType.Types.URL.getTypeName())){
                statement.setURL(paraIndex,new URL("https://127.0.0.1:80/hello"));
            }else {
                statement.setObject(paraIndex,new Object());
            }

        }
        catch (MalformedURLException mlfe){
            logger.error("Exception occurs in setting URL parameter for the PrepareStatement.",mlfe);
        }catch (SQLException sqle){
            logger.error("Exception occurs in building parameter for PrepareStatement from ResultSet.",sqle);
        }

        return statement;
    }
}