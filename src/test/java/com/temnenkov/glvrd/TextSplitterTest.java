package com.temnenkov.glvrd;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class TextSplitterTest {

    private TextSplitter s;

    @Before
    public void setUp() throws Exception {
        s = new TextSplitter();
    }

    @Test
    public void splitInChunks4() throws Exception {
        List<String> r = s.splitInChunks("1234<b>56</b>78<b>9</b>10a11", 4);

        assertThat(r.size(), is(equalTo(2)));
        assertThat(r.get(0), is(equalTo("1234")) );
        assertThat(r.get(1), is(equalTo("<b>56</b>78<b>9</b>10a11")) );
    }

    @Test
    public void splitInChunks5() throws Exception {
        List<String> r = s.splitInChunks("1234<b>56</b>78<b>9</b>10a11", 5);

        assertThat(r.size(), is(equalTo(2)));
        assertThat(r.get(0), is(equalTo("1234")) );
        assertThat(r.get(1), is(equalTo("<b>56</b>78<b>9</b>10a11")) );
    }

    @Test
    public void justSplit() throws Exception {
        final String input = "Исходный текст:\n\nШкаф АТС-АЛС выполнен в виде вандалоустойчивого внешнего шкафа, в который устанавливается отдельный отсек с Li-Fe-Po АКБ - 3 секции по 20 <b>А/ч</b> 48 В и гермитичный внутренний шкаф с <b>активным</b> оборудованием и кроссом. Внешний корпус - сталь 1,2 мм в жалюзи дверь, задняя стенка и боковины - верх и низ, дверь с ригелями на 3 стороны. Внутренний корпус - алюминий 0,5 мм, зазор по 50 мм со <b>всех</b> сторон.\n\nОбнаружено 3 замечания.\n\n<b>А/ч</b>: неверное использование косой черты (косая черта используется только в сокращениях типа км/ч)\n<b>активным</b>: необъективная оценка (объясните, что означает это определение)\n<b>всех</b>: обобщение (используйте только в сравнении с частью.)\n\nСтатистика:\n328 знаков (без пробелов), 396 знаков (с пробелами)";
        final List<String> r = s.splitInChunks(input, 4096);
        assertThat(r.size(), is(equalTo(1)));
        assertThat(r.get(0), is(equalTo(input)) );
    }

    @Test
    public void splitInChunks9() throws Exception {
        List<String> r = s.splitInChunks("1234<b>56</b>78<b>9</b>10a11", 9);

        assertThat(r.size(), is(equalTo(5)));
        assertThat(r.get(0), is(equalTo("1234")) );
        assertThat(r.get(1), is(equalTo("<b>56</b>")) );
        assertThat(r.get(2), is(equalTo("78")) );
        assertThat(r.get(3), is(equalTo("<b>9</b>1")) );
        assertThat(r.get(4), is(equalTo("0a11")) );
    }

    @Test
    public void splitInChunks2() throws Exception {
        List<String> r = s.splitInChunks("12\n34\n5\n6\n78\n90", 2);

        assertThat(r.size(), is(equalTo(8)));
        assertThat(r.get(0), is(equalTo("12")) );
        assertThat(r.get(1), is(equalTo("\n3")) );
        assertThat(r.get(2), is(equalTo("4\n")) );
        assertThat(r.get(3), is(equalTo("5\n")) );
        assertThat(r.get(4), is(equalTo("6\n")) );
        assertThat(r.get(5), is(equalTo("78")) );
        assertThat(r.get(6), is(equalTo("\n9")) );
        assertThat(r.get(7), is(equalTo("0")) );
    }

    @Test
    public void testSimple() throws Exception {
        Pattern BOLD = Pattern.compile("<b>(.*?)</b>");
        List<BadFrag> r = s.mark("123 <b>56</b>78<b>9</b>10 11", BOLD);

        assertThat(s.test(r, 0), is(equalTo(true))); // 1
        assertThat(s.test(r, 1), is(equalTo(true))); // 2
        assertThat(s.test(r, 2), is(equalTo(true))); // 3
        assertThat(s.test(r, 3), is(equalTo(true))); //
        assertThat(s.test(r, 4), is(equalTo(true))); // <
        assertThat(s.test(r, 5), is(equalTo(false))); // b
        assertThat(s.test(r, 6), is(equalTo(false))); // >
        assertThat(s.test(r, 7), is(equalTo(false))); // 5
        assertThat(s.test(r, 8), is(equalTo(false))); // 6
        assertThat(s.test(r, 9), is(equalTo(false))); // <
        assertThat(s.test(r, 10), is(equalTo(false))); // /
        assertThat(s.test(r, 11), is(equalTo(false))); // b
        assertThat(s.test(r, 12), is(equalTo(false))); // >
        assertThat(s.test(r, 13), is(equalTo(true))); // 7
        assertThat(s.test(r, 14), is(equalTo(true))); // 8
        assertThat(s.test(r, 15), is(equalTo(true))); // <
        assertThat(s.test(r, 16), is(equalTo(false))); // b
        assertThat(s.test(r, 17), is(equalTo(false))); // >
        assertThat(s.test(r, 18), is(equalTo(false))); // 9
        assertThat(s.test(r, 19), is(equalTo(false))); // <
        assertThat(s.test(r, 20), is(equalTo(false))); // /
        assertThat(s.test(r, 21), is(equalTo(false))); // b
        assertThat(s.test(r, 22), is(equalTo(false))); // >
        assertThat(s.test(r, 23), is(equalTo(true))); // 1
        assertThat(s.test(r, 24), is(equalTo(true))); // 0
        assertThat(s.test(r, 25), is(equalTo(true))); //
        assertThat(s.test(r, 26), is(equalTo(true))); // 1
        assertThat(s.test(r, 27), is(equalTo(true))); // 1
        assertThat(s.test(r, 28), is(equalTo(true))); // .
        assertThat(s.test(r, 29), is(equalTo(true))); // .
        assertThat(s.test(r, 30), is(equalTo(true))); // .
        assertThat(s.test(r, 31), is(equalTo(true))); // .

    }

    @Test
    public void markBold() throws Exception {
        Pattern BOLD = Pattern.compile("<b>(.*?)</b>");
        final String input = "123 <b>56</b>78<b>9</b>10 11";
        List<BadFrag> r = s.mark(input, BOLD);

        assertThat(r.size(), is(equalTo(2)));
        assertThat(r.get(0), is(equalTo(new BadFrag("<b>56</b>", 4, 13))));
        assertThat(r.get(1), is(equalTo(new BadFrag("<b>9</b>", 15, 23))));

        assertThat(input.substring(r.get(0).getStart(), r.get(0).getEnd()), is(equalTo(r.get(0).getText())));
        assertThat(input.substring(r.get(1).getStart(), r.get(1).getEnd()), is(equalTo(r.get(1).getText())));
    }

    @Test
    public void markLine() throws Exception {
        Pattern LINE = Pattern.compile("\\n");
        final String input = "123\n\n45\n";
        List<BadFrag> r = s.mark(input, LINE);

        assertThat(r.size(), is(equalTo(3)));
        assertThat(r.get(0), is(equalTo(new BadFrag("\n", 3, 4))));
        assertThat(r.get(1), is(equalTo(new BadFrag("\n", 4, 5))));
        assertThat(r.get(2), is(equalTo(new BadFrag("\n", 7, 8))));

        assertThat(input.substring(r.get(0).getStart(), r.get(0).getEnd()), is(equalTo(r.get(0).getText())));
        assertThat(input.substring(r.get(1).getStart(), r.get(1).getEnd()), is(equalTo(r.get(1).getText())));
        assertThat(input.substring(r.get(2).getStart(), r.get(2).getEnd()), is(equalTo(r.get(2).getText())));
    }

}