
/** @author Jaroslaw Hirniak */

package group2.sdp.util;

import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.PointSet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class PrettyPrint
{
    
    private StringBuilder sb;
    private int n, column, columnFirst, space, precision;
    private boolean first = true, firstDifferent = false;
    
    public PrettyPrint()
    { 
        sb = new StringBuilder();
        precision = space = 3;
    }
    
    @Override
    public String toString()
    { return sb.toString(); }
    
    private String getFormat()
    { return getFormat(column); }
    
    private String getFormat(int column)
    { return "%" + Integer.toString(column) + "s"; }
    
    private String getFormat(double d)
    {
        String format = "%" + "." +
                Integer.toString(precision) + "f";
        return format;
    }
    
    private void pad()
    { pad(space); }
    
    public void pad(int padding)
    {
        if (first)
        { return; }
        
        for (int i = 0; i < padding; i++)
        { sb.append(" "); }
    }
    
    public void setFirstColumnWidth(String... ss)
    {
        firstDifferent = true;
        for (String s : ss)
        { columnFirst = Math.max(columnFirst, width(s)); }
    }
    
    public void setColumnWidth(int width)
    { this.column = width; }
    
    public void evaluateWidth(double n)
    { this.column = width(n); }
    
    public void evaluateWidth(PointSet points)
    { this.column = width(points); }
    
    public void evaluateWidth(Object... obs)
    {
        for (Object o : obs)
        { this.column = Math.max(column, width(o)); }
    }
    
    public void setColumnsNumber(int n)
    { this.n = n; }
    
    public void setSpace(int space)
    { this.space = space; }
    
    public void append(String s)
    { sb.append(s); }
    
    public void appendCell(long n)
    {
        pad();
        sb.append(String.format(getFormat(), n));
        first = false;
    }
    
    public void appendCell(double d)
    {
        pad();
        pad(column - width(d));
        String txt = String.format(getFormat(d), d);
        sb.append(txt);
        first = false;
    }
    
    public void appendCell(Double d)
    {
       appendCell((double) d);
    }
    
    public void appendCell(Object o)
    { 
        pad();
        sb.append(String.format(getFormat(), o));
        first = false;
    }
    
    public void appendLabel(String l)
    {
        if (first)
        {
            String label = String.format("%" + Integer.toString(columnFirst)
                    + "s", l);
            sb.append(label);
            first = false;
        }
    }
    
    public void appendCell(Object o, int rowspan)
    {
        int firstModifier = firstDifferent ? columnFirst - column : 0;
        int totalWidth = column * rowspan + space * (rowspan - 1) + firstModifier;
        int textWidth  = o.toString().length();
        int leftPadding = (totalWidth - textWidth) / 2;
        int rightPadding = totalWidth - textWidth - leftPadding;

        first = false;
        pad(leftPadding);
        sb.append(o);
        pad(rightPadding);
    }
    
    public void newRow()
    { 
        sb.append("\n");
        first = true;
    }
    
    public void appendRow(Object... labels)
    {
        for (Object label : labels)
        { appendCell(label); }
        newRow();
    }
    
    public void appendRow(String[]... labelGroups)
    {
        for (String labels[] : labelGroups)
        {
            for (String label : labels)
            { appendCell(label); }
        }
        newRow();
    }
    
    public void appendTitle(String title)
    {
        appendCell(title, n);
        newRow();
    }
    
    /**
     * @return number of digits in the number, plus one if negative
     */
    
    private int width(double n)
    {
        int width = width((Number) n) + precision + 1;
        return width;
    }
    
    private int width(Number n)
    {
        double number = Math.abs(n.doubleValue());
        int negative = n.doubleValue() < 0 ? 1 : 0;
        int digits = (number < 10.0) ? 1 : (int) Math.log10(number) + 1;
        return negative + digits;
    }
    
    private int width(PointSet ps)
    {
        int width = 0;
        
        for (Point p : ps.getPoints())
        { width = Math.max(width, Math.max(width(p.getX()), width(p.getY()))); }
        
        return width;
    }
    
    private int width(Object o)
    { return o.toString().length(); }
    
    private static void prepareFolder(String folderName)
    {
        File folder = new File(folderName);
        
        if (!folder.exists())
        { folder.mkdir(); }
    }
    
    public void save(String... filePaths)
    {
        String content = sb.toString();
        for (String file : filePaths)
        { saveContent(file, content); }
    }
    
    private void saveContent(String file, String content)
    {
        if (file.lastIndexOf(File.separator) != -1)
        {
            String folder = file.substring(0, file.lastIndexOf(File.separator));
            prepareFolder(folder);
        }
        
        BufferedWriter fs = null;
        
        try
        {
            System.out.println("Saving text file to " + file + "...");
            fs = new BufferedWriter(new FileWriter(file));
            fs.write(content);
        }
        catch (FileNotFoundException e)
        { e.printStackTrace(); }
        
        catch (IOException e)
        { e.printStackTrace(); }
        
        finally
        {
            try
            {
                if (fs != null)
                { fs.close(); }
            }
            
            catch (IOException e)
            { e.printStackTrace(); }
        }
    }

}

