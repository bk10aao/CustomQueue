import os
import matplotlib.pyplot as plt
from matplotlib.lines import Line2D
import pandas as pd

# ---------------------------------------------------------------------------
# Paths
# ---------------------------------------------------------------------------
script_dir = os.path.dirname(os.path.abspath(__file__))
custom_csv = os.path.join(script_dir, "CustomQueueV2_wide_matrix.csv")
jdk_csv = os.path.join(script_dir, "CustomQueue_wide_matrix.csv")

# ---------------------------------------------------------------------------
# Load CSV results
# ---------------------------------------------------------------------------
custom_df = pd.read_csv(custom_csv, sep=';', engine='python')
jdk_df = pd.read_csv(jdk_csv, sep=';', engine='python')

# Clean column names (remove quotes if present)
custom_df.columns = [col.strip().replace('"', '') for col in custom_df.columns]
jdk_df.columns = [col.strip().replace('"', '') for col in jdk_df.columns]

# Ensure Size is present and sort by Size
if "Size" in custom_df.columns:
    custom_df = custom_df.sort_values("Size")
if "Size" in jdk_df.columns:
    jdk_df = jdk_df.sort_values("Size")

# Common columns to plot (excluding Size and Winner if present)
exclude_cols = {"Size", "Winner"}
valid_cols = [
    col for col in custom_df.columns
    if col not in exclude_cols and col in jdk_df.columns
]

# ---------------------------------------------------------------------------
# Plotting
# ---------------------------------------------------------------------------
color_custom = "#ff4d4d"  # Red for Custom Queue V2
color_native = "#4da6ff"  # Blue for Custom Queue V1

for method in valid_cols:
    fig, ax = plt.subplots(figsize=(8, 5.5))

    ax.plot(
        custom_df["Size"],
        custom_df[method],
        color=color_custom,
        marker="o",
        markersize=5,
        linestyle="-",
        linewidth=2,
    )
    ax.plot(
        jdk_df["Size"],
        jdk_df[method],
        color=color_native,
        marker="o",
        markersize=5,
        linestyle="-",
        linewidth=2,
    )

    ax.set_xlim(left=custom_df["Size"].min(), right=custom_df["Size"].max())

    title = method

    # Dark-mode friendly styling
    ax.set_title(title, fontsize=14, fontweight="bold", color="white", pad=15)
    ax.set_xlabel("Size", fontsize=11, color="white")
    ax.set_ylabel("Time (ns/op)", fontsize=11, color="white")
    ax.tick_params(axis="both", colors="white")
    ax.grid(True, linestyle="--", alpha=0.3, color="white")
    for spine in ax.spines.values():
        spine.set_color("white")

    legend_elements = [
        Line2D(
            [0],
            [0],
            marker="o",
            color="none",
            label="V1",
            markerfacecolor=color_custom,
            markeredgecolor=color_custom,
            markersize=8,
            linestyle="None",
        ),
        Line2D(
            [0],
            [0],
            marker="o",
            color="none",
            label="V2",
            markerfacecolor=color_native,
            markeredgecolor=color_native,
            markersize=8,
            linestyle="None",
        ),
    ]
    legend = ax.legend(
        handles=legend_elements,
        loc="upper center",
        bbox_to_anchor=(0.5, -0.15),
        fontsize=10,
        frameon=False,
        ncol=2,
    )
    for text in legend.get_texts():
        text.set_color("white")

    fig.patch.set_alpha(0.0)
    ax.patch.set_alpha(0.0)

    plt.tight_layout()

    safe_filename = (
        method.replace("(", "_")
        .replace(")", "_")
        .replace(",", "_")
        .replace(".", "_")
        .replace(" ", "_")
        .replace("[", "_")
        .replace("]", "_")
    )
    output_image_path = os.path.join(script_dir, f"plot_{safe_filename}.png")
    plt.savefig(output_image_path, transparent=True, bbox_inches="tight")
    plt.close()

print(f"Successfully generated {len(valid_cols)} performance graphs in {script_dir}")