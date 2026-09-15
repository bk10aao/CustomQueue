import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from scipy.stats import gmean

# Load wide matrix CSV files directly
custom_df = pd.read_csv('CustomQueueV2_wide_matrix.csv', sep=';')
jdk_df = pd.read_csv('CustomQueue_wide_matrix.csv', sep=';')

# Clean column names
custom_df.columns = [col.strip().replace('"', '') for col in custom_df.columns]
jdk_df.columns = [col.strip().replace('"', '') for col in jdk_df.columns]

# Align common sizes
common_sizes = set(custom_df['Size']).intersection(set(jdk_df['Size']))
custom_df = (
    custom_df[custom_df['Size'].isin(common_sizes)]
    .sort_values('Size')
    .reset_index(drop=True)
)
jdk_df = (
    jdk_df[jdk_df['Size'].isin(common_sizes)]
    .sort_values('Size')
    .reset_index(drop=True)
)

# Extract methods and exclude Constructor(int) if present
methods = [col for col in custom_df.columns if col != 'Size' and col != 'Constructor(int)']

custom_df_fixed = custom_df.copy()
jdk_df_fixed = jdk_df.copy()

for col in methods:
    custom_df_fixed[col] = custom_df_fixed[col].replace(0, 1)
    jdk_df_fixed[col] = jdk_df_fixed[col].replace(0, 1)

ratios = []
labels = []
colors = []

# Custom V1 wins (red), Custom V2 wins (blue)
v1_win_color = '#FF4D4D'
v2_win_color = '#4DA6FF'

for m in methods:
    g_v2 = gmean(custom_df_fixed[m])
    g_v1 = gmean(jdk_df_fixed[m])

    if g_v1 < g_v2:
        speedup = g_v2 / g_v1
        ratios.append(-(speedup - 1))
        colors.append(v1_win_color)
    else:
        speedup = g_v1 / g_v2
        ratios.append(speedup - 1)
        colors.append(v2_win_color)
    labels.append(m)

sorted_indices = np.argsort(ratios)
sorted_ratios = [ratios[idx] for idx in sorted_indices]
sorted_labels = [labels[idx] for idx in sorted_indices]
sorted_colors = [colors[idx] for idx in sorted_indices]

min_ratio = min(sorted_ratios)
max_ratio = max(sorted_ratios)

# Extra margin on sides so annotations fit cleanly
left_limit = min(min_ratio - 0.1, -1.2)
right_limit = max(max_ratio + 0.1, 1.2)

fig_height = max(6, len(methods) * 0.45)
fig, ax = plt.subplots(figsize=(12, fig_height), facecolor='none')
ax.set_facecolor('none')

bars = ax.barh(
    range(len(sorted_labels)),
    sorted_ratios,
    color=sorted_colors,
    alpha=0.9,
    height=0.6,
)

ax.axvline(x=0, color='#ffffff', linewidth=1.2)
ax.set_xlim(left_limit, right_limit)

ticks = []
if left_limit < -1.0:
    ticks.append(-1.0)
ticks.append(0.0)
for t in [1.0, 2.0, 3.0, 4.0]:
    if t <= right_limit:
        ticks.append(t)

ax.set_xticks(ticks)
ax.set_xticklabels(
    [f'{abs(t) + 1:.1f}x' if abs(t) > 0.05 else 'Tie' for t in ticks],
    color='#ffffff',
    fontsize=11,
)

ax.set_ylim(-0.5, len(methods) - 0.5)
ax.set_yticks(range(len(sorted_labels)))
ax.set_yticklabels(sorted_labels, color='#ffffff', fontsize=10)

# Add exact numeric speedup values directly next to each bar
for idx, (bar, r) in enumerate(zip(bars, sorted_ratios)):
    val = abs(r)
    if val < 0.02:
        text_str = 'Tie'
    else:
        factor = val + 1
        if r < 0:
            text_str = f'Custom V1 {factor:.2f}x'
        else:
            text_str = f'Custom V2 {factor:.2f}x'

    if r >= 0:
        ax.text(
            r + 0.02,
            idx,
            f'  {text_str}',
            va='center',
            ha='left',
            color='#ffffff',
            fontsize=9,
            fontweight='bold',
        )
    else:
        ax.text(
            r - 0.02,
            idx,
            f'{text_str}  ',
            va='center',
            ha='right',
            color='#ffffff',
            fontsize=9,
            fontweight='bold',
        )

ax.set_title(
    (
        'Overall Relative Performance Comparison (customqueue V1 vs customqueue V2)\n'
        '(Geometric Mean Across All Sizes)'
    ),
    fontsize=14,
    fontweight='bold',
    pad=15,
    color='#ffffff',
)
ax.set_xlabel(
    '← Custom V1 Faster  |  Relative Speedup Factor  |  Custom V2 Faster →',
    fontsize=12,
    labelpad=10,
    color='#ffffff',
)
ax.grid(True, axis='x', linestyle='--', alpha=0.3, color='#888888')
ax.tick_params(colors='#ffffff', which='both', length=0)
for spine in ax.spines.values():
    spine.set_edgecolor('#555555')

plt.tight_layout()
plt.savefig('geometric.png', dpi=300, transparent=True)
plt.close()

print('Generated geometric comparison graph successfully without Constructor(int)!')