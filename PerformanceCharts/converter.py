from pathlib import Path
import pandas as pd

HEADER_MAPPING = {
    # Constructors
    "testConstructorDefault": "Constructor()",
    "testConstructorCapacity": "Constructor(int)",
    "testConstructorCollection": "Constructor(Collection)",

    # Operations
    "testAdd": "add(E)",
    "testAddAll": "addAll(Collection)",
    "testClear": "clear()",
    "testContains": "contains(Object)",
    "testContainsAll": "containsAll(Collection)",
    "testElement": "element()",
    "testIsEmpty": "isEmpty()",
    "testIterator": "iterator()",
    "testOffer": "offer(E)",
    "testPeek": "peek()",
    "testPoll": "poll()",
    "testRemoveHead": "remove()",
    "testRemoveObject": "remove(Object)",
    "testRemoveAll": "removeAll(Collection)",
    "testRetainAll": "retainAll(Collection)",
    "testSize": "size()",
    "testToArray": "toArray()",
    "testToArrayWithType": "toArray(T[])",
    "testToString": "toString()"
}

COLUMN_ORDER = [
    "Constructor()", "Constructor(int)", "Constructor(Collection)",
    "add(E)", "addAll(Collection)", "clear()", "contains(Object)",
    "containsAll(Collection)", "element()", "isEmpty()", "iterator()",
    "offer(E)", "peek()", "poll()", "remove()", "remove(Object)",
    "removeAll(Collection)", "retainAll(Collection)", "size()",
    "toArray()", "toArray(T[])", "toString()"
]


def convert_to_wide_matrix(input_file: str | Path, output_file: str | Path) -> None:
    input_path = Path(input_file)
    output_path = Path(output_file)

    if not input_path.exists():
        print(f"Skipping '{input_path}' (file not found).")
        return

    df = pd.read_csv(input_path, sep=';')

    # Extract short method name from fully-qualified package string
    clean_benchmark = df['Benchmark'].astype(str).str.strip('"\t ')
    method_names = clean_benchmark.str.split('.').str[-1]

    # Map method names using HEADER_MAPPING with raw name fallback
    df['Metric'] = method_names.map(HEADER_MAPPING).fillna(method_names)

    # Pivot table and aggregate scores safely using mean
    pivot_df = df.pivot_table(
        index='Size',
        columns='Metric',
        values='Score (ns/op)',
        aggfunc='mean'
    ).round()

    # Safely convert to nullable Int64 to avoid crashes on missing/NaN cells
    pivot_df = pivot_df.astype('Int64')

    # Reorder according to COLUMN_ORDER while appending any unmapped extra columns
    available_columns = [col for col in COLUMN_ORDER if col in pivot_df.columns]
    extra_columns = [col for col in pivot_df.columns if col not in available_columns]
    pivot_df = pivot_df.reindex(columns=available_columns + extra_columns)

    # Export wide matrix CSV
    pivot_df.to_csv(output_path, sep=';')
    print(f"Wide matrix CSV created: {output_path}")


if __name__ == "__main__":
    convert_to_wide_matrix("CustomQueue_jmh_performance.csv", "CustomQueue_wide_matrix.csv")
    convert_to_wide_matrix("CustomQueueV2_jmh_performance.csv", "CustomQueueV2_wide_matrix.csv")