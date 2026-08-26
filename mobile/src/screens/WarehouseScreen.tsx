import React, { useState } from 'react';
import { View, Text, StyleSheet, SafeAreaView, TouchableOpacity, TextInput, FlatList } from 'react-native';

export default function WarehouseScreen() {
  const [skuSearch, setSkuSearch] = useState('');
  const [scanResult, setScanResult] = useState<string | null>(null);

  const mockItems = [
    { code: 'RM-TH-01', name: 'Heavy Cotton Stitching Thread', stock: '500 spools', location: 'Rack A-2' },
    { code: 'RM-FAB-02', name: 'Unbleached Raw Denim Fabric', stock: '320 meters', location: 'Vault B' },
    { code: 'CS-NK-LBL', name: 'Zara Brand Care Labels', stock: '2500 pcs', location: 'Client Store 1' }
  ];

  const handleSimulateScan = () => {
    setScanResult('Scanned Tag: CS-NK-LBL (Zara Brand Care Labels)');
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.title}>Warehouse Mobile Hub</Text>
        <Text style={styles.subtitle}>Barcode Lookup & Bin Transfers</Text>

        <TouchableOpacity style={styles.scanBtn} onPress={handleSimulateScan}>
          <Text style={styles.scanBtnText}>CAMERA BARCODE SCANNER</Text>
        </TouchableOpacity>

        {scanResult ? (
          <View style={styles.scanResultBox}>
            <Text style={styles.scanResultText}>{scanResult}</Text>
          </View>
        ) : null}

        <Text style={styles.sectionLabel}>Material Lookup</Text>
        <TextInput
          style={styles.input}
          placeholder="Type SKU or barcode..."
          value={skuSearch}
          onChangeText={setSkuSearch}
        />

        <FlatList
          data={mockItems.filter(i => i.code.toLowerCase().includes(skuSearch.toLowerCase()))}
          keyExtractor={(item) => item.code}
          renderItem={({ item }) => (
            <View style={styles.itemCard}>
              <View style={styles.itemRow}>
                <Text style={styles.itemCode}>{item.code}</Text>
                <Text style={styles.itemStock}>{item.stock}</Text>
              </View>
              <Text style={styles.itemName}>{item.name}</Text>
              <Text style={styles.itemLoc}>Bin: {item.location}</Text>
            </View>
          )}
        />
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f8fafc' },
  content: { flex: 1, padding: 20 },
  title: { fontSize: 22, fontWeight: '800', color: '#0f172a' },
  subtitle: { fontSize: 12, color: '#64748b', marginTop: 2, marginBottom: 16 },
  scanBtn: { backgroundColor: '#0284c7', padding: 18, borderRadius: 12, alignItems: 'center', marginBottom: 16 },
  scanBtnText: { color: '#ffffff', fontWeight: '900', fontSize: 14 },
  scanResultBox: { backgroundColor: '#e0f2fe', padding: 12, borderRadius: 10, marginBottom: 16 },
  scanResultText: { color: '#0369a1', fontSize: 12, fontWeight: '700' },
  sectionLabel: { fontSize: 11, fontWeight: '700', color: '#94a3b8', textTransform: 'uppercase', marginBottom: 8 },
  input: { backgroundColor: '#ffffff', borderWidth: 1, borderColor: '#cbd5e1', borderRadius: 10, padding: 12, fontSize: 14, marginBottom: 16 },
  itemCard: { backgroundColor: '#ffffff', borderWidth: 1, borderColor: '#cbd5e1', borderRadius: 12, padding: 14, marginBottom: 10 },
  itemRow: { flexDirection: 'row', justifyContent: 'space-between' },
  itemCode: { fontSize: 12, fontWeight: '800', color: '#4f46e5' },
  itemStock: { fontSize: 12, fontWeight: '800', color: '#0f172a' },
  itemName: { fontSize: 13, fontWeight: '700', color: '#334155', marginTop: 4 },
  itemLoc: { fontSize: 11, color: '#64748b', marginTop: 2 }
});
