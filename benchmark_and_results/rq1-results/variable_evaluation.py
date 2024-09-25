import pandas as pd

# 读取CSV文件
file_path = '/usr/local/Lcg-src/rq1/storm_rq1dd.csv'  # 替换为你的CSV文件路径
data = pd.read_csv(file_path)

def calculate_metrics(removed_vars, inserted_vars):
    # 转换为集合
    removed_set = set(removed_vars)
    inserted_set = set(inserted_vars)
    
    # 计算True Positives (TP), False Positives (FP), False Negatives (FN)
    tp = len(removed_set & inserted_set)  # Intersection
    fp = len(inserted_set - removed_set)  # Inserted but not removed
    fn = len(removed_set - inserted_set)  # Removed but not inserted
    
    # 检查分母是否为0
    precision_denom = tp + fp
    recall_denom = tp + fn
    
    # 如果分母为0，返回None
    if precision_denom == 0 or recall_denom == 0:
        return None, None, None
    
    # Precision, Recall 和 F1 Score计算
    precision = tp / precision_denom
    recall = tp / recall_denom
    f1 = 2 * precision * recall / (precision + recall) if (precision + recall) > 0 else 0
    
    return precision, recall, f1

# 计算每一行的Precision、Recall和F1 Score
results = []
skipped_cases = 0  # 记录跳过的案例数

for _, row in data.iterrows():
    # 处理NaN值
    removed_vars = row['removed-vairable'].split() if pd.notna(row['removed-vairable']) else []
    inserted_vars = row['inserted-vairable'].split() if pd.notna(row['inserted-vairable']) else []
    
    precision, recall, f1 = calculate_metrics(removed_vars, inserted_vars)
    
    # 如果结果为None，跳过该行
    if precision is None or recall is None or f1 is None:
        skipped_cases += 1
        continue
    
    # if precision is not None:
    results.append({
        'Precision': precision,
        'Recall': recall,
        'F1 Score': f1
    })

# 将结果转换为DataFrame
results_df = pd.DataFrame(results)

# 计算均值
mean_precision = results_df['Precision'].mean()
mean_recall = results_df['Recall'].mean()
mean_f1 = results_df['F1 Score'].mean()

# 打印结果
print(results_df)
print(f'\nMean Precision: {mean_precision:.3f}')
print(f'Mean Recall: {mean_recall:.3f}')
print(f'Mean F1 Score: {mean_f1:.3f}')
print(f'Skipped cases: {skipped_cases}')
