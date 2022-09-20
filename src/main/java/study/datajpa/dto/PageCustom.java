package study.datajpa.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class PageCustom<T> implements Serializable {
    private List<T> content;

    private boolean hasNext;

    private int totalPages;

    private long totalElements;

    private int page;

    private int size;

    private boolean first;

    private boolean last;

}
